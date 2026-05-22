package archive.backend.domain.record.service.impl;

import archive.backend.domain.record.service.RecordService;
import archive.backend.domain.record.service.RecordTagVO;
import archive.backend.domain.record.service.RecordVO;
import archive.backend.domain.record.service.dto.RecordDetailResponse;
import archive.backend.domain.record.service.dto.RecordListSearchCondition;
import archive.backend.domain.record.service.dto.RecordReferenceSearchCondition;
import archive.backend.domain.record.service.dto.RecordRequest;
import archive.backend.domain.record.service.dto.RecordSearchOptionResponse;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private static final Set<String> ALLOWED_RECORD_TYPES = Set.of("urban", "major", "extra");
    private static final Set<String> ALLOWED_REFERENCE_SEARCH_TYPES = Set.of("name", "subject");
    private static final Set<String> ALLOWED_LIST_SEARCH_TYPES = Set.of("title", "name", "subject");

    private final RecordDAO dao;

    private final AmazonS3 amazonS3;
    private final Dotenv dotenv;

    @Override
    @Transactional
    public boolean insertRecord(String loginId, RecordRequest request, MultipartFile file) {
        try {
            String normalizedType = normalizeRecordType(request.getType());
            if (normalizedType == null) {
                return false;
            }

            RecordVO record = new RecordVO();
            record.setUserNumber(findUserNumberByLoginId(loginId));
            record.setType(normalizedType);
            record.setSubjectNumber(request.getSubjectNumber());
            record.setTitle(request.getTitle());
            record.setDescription(request.getDescription());
            record.setGrade(request.getGrade());
            record.setSemester(request.getSemester());

            if (file != null && !file.isEmpty()) {
                record.setFileUrl(uploadFile(file));
            }

            dao.insertRecord(record);

            if (request.getTaggedUserNumbers() != null && !request.getTaggedUserNumbers().isEmpty()) {
                for (Long targetUserNum : request.getTaggedUserNumbers()) {
                    dao.insertUserTag(record.getRecordNumber(), targetUserNum);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordSearchOptionResponse> searchRecordReferences(String searchType, String keyword) {
        String normalizedSearchType = normalizeSearchType(searchType, ALLOWED_REFERENCE_SEARCH_TYPES);
        String normalizedKeyword = normalizeKeyword(keyword);

        if (normalizedSearchType == null || normalizedKeyword == null) {
            return new ArrayList<>();
        }

        return dao.searchRecordReferences(new RecordReferenceSearchCondition(normalizedSearchType, normalizedKeyword));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordDetailResponse> selectRecordList(String type, String searchType, String keyword) {
        String normalizedType = normalizeOptionalRecordType(type);
        if (type != null && normalizedType == null) {
            return new ArrayList<>();
        }

        String normalizedKeyword = normalizeKeyword(keyword);
        String normalizedSearchType = normalizeSearchType(searchType, ALLOWED_LIST_SEARCH_TYPES);

        if (normalizedKeyword != null && normalizedSearchType == null) {
            return new ArrayList<>();
        }

        RecordListSearchCondition condition = new RecordListSearchCondition(
                normalizedType,
                normalizedSearchType,
                normalizedKeyword
        );

        List<RecordVO> records = dao.selectRecordList(condition).stream()
                .peek(record -> record.setType(normalizeRecordType(record.getType())))
                .filter(record -> record.getType() != null)
                .toList();
        if (records.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> ids = records.stream().map(RecordVO::getRecordNumber).toList();

        Map<Long, List<Long>> tagMap = dao
                .selectTagsByRecordNumbers(ids).stream()
                .collect(Collectors.groupingBy(RecordTagVO::getRecordNumber,
                        Collectors.mapping(RecordTagVO::getUserNumber, Collectors.toList())));

        return records.stream()
                .map(record -> new RecordDetailResponse(
                        record,
                        tagMap.getOrDefault(record.getRecordNumber(), new ArrayList<>())
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecordDetailResponse selectRecordDetail(Long recordNumber) {
        RecordVO record = dao.selectRecordDetail(recordNumber);
        if (record == null) {
            return null;
        }

        record.setType(normalizeRecordType(record.getType()));
        if (record.getType() == null) {
            return null;
        }

        List<Long> taggedUserNumbers = dao.selectTaggedUserNumbers(recordNumber);

        RecordDetailResponse response = new RecordDetailResponse();
        response.setRecord(record);
        response.setTaggedUserNumbers(taggedUserNumbers);

        return response;
    }

    @Override
    @Transactional
    public boolean updateRecord(Long recordNumber, String loginId, boolean admin, RecordRequest request, MultipartFile file) {
        RecordVO record = dao.selectRecordDetail(recordNumber);
        if (record == null) {
            return false;
        }

        String normalizedType = normalizeRecordType(request.getType());
        if (normalizedType == null) {
            return false;
        }

        if (!admin && !record.getUserNumber().equals(findUserNumberByLoginId(loginId))) {
            throw new AccessDeniedException("본인 기록만 수정할 수 있습니다.");
        }

        if (file != null && !file.isEmpty()) {
            if (record.getFileUrl() != null) {
                deleteFileFromS3(record.getFileUrl());
            }
            record.setFileUrl(uploadFile(file));
        }

        record.setType(normalizedType);
        record.setSubjectNumber(request.getSubjectNumber());
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setGrade(request.getGrade());
        record.setSemester(request.getSemester());

        dao.updateRecord(record);

        dao.deleteTagsByRecordNumber(recordNumber);

        if (request.getTaggedUserNumbers() != null && !request.getTaggedUserNumbers().isEmpty()) {
            for (Long userNum : request.getTaggedUserNumbers()) {
                dao.insertUserTag(recordNumber, userNum);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean deleteRecord(Long recordNumber, String loginId, boolean admin) {
        RecordVO record = dao.selectRecordDetail(recordNumber);
        if (record == null) {
            return false;
        }

        if (!admin && !record.getUserNumber().equals(findUserNumberByLoginId(loginId))) {
            throw new AccessDeniedException("본인 기록만 삭제할 수 있습니다.");
        }

        if (record.getFileUrl() != null) {
            deleteFileFromS3(record.getFileUrl());
        }
        dao.deleteRecord(recordNumber);
        return true;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String bucket = dotenv.get("S3_BUCKET_NAME");
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(bucket, fileName, inputStream, metadata);
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            return "false";
        }
    }

    @Override
    public void deleteFileFromS3(String fileUrl) {
        try {
            String bucket = dotenv.get("S3_BUCKET_NAME");
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long findUserNumberByLoginId(String loginId) {
        return dao.findUserNumberByLoginId(loginId);
    }

    private String normalizeOptionalRecordType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }
        return normalizeRecordType(type);
    }

    private String normalizeRecordType(String type) {
        if (type == null) {
            return null;
        }

        String normalizedType = type.trim().toLowerCase(Locale.ROOT);
        if (!ALLOWED_RECORD_TYPES.contains(normalizedType)) {
            return null;
        }
        return normalizedType;
    }

    private String normalizeSearchType(String searchType, Set<String> allowedTypes) {
        if (searchType == null || searchType.isBlank()) {
            return null;
        }

        String normalizedSearchType = searchType.trim().toLowerCase(Locale.ROOT);
        if (!allowedTypes.contains(normalizedSearchType)) {
            return null;
        }
        return normalizedSearchType;
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }

        String normalizedKeyword = keyword.trim();
        if (normalizedKeyword.isEmpty()) {
            return null;
        }
        return normalizedKeyword;
    }
}
