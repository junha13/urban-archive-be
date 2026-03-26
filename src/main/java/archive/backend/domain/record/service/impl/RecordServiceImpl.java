package archive.backend.domain.record.service.impl;


import archive.backend.domain.record.service.RecordService;
import archive.backend.domain.record.service.RecordTagVO;
import archive.backend.domain.record.service.RecordVO;
import archive.backend.domain.record.service.dto.RecordDetailResponse;
import archive.backend.domain.record.service.dto.RecordRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordDAO dao;

    private final AmazonS3 amazonS3;
    private final Dotenv dotenv;

    @Override
    @Transactional
    public boolean insertRecord(RecordRequest request, MultipartFile file) {
        try {
            RecordVO record = new RecordVO();
            record.setUserNumber(request.getUserNumber());
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
                    // DAO의 insertUserTag(게시글번호, 태그된유저번호)
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
    @Transactional
    public List<RecordDetailResponse> selectRecordList() {
        List<RecordVO> records = dao.selectRecordList();
        if (records.isEmpty()) return new ArrayList<>();

        List<Long> ids = records.stream().map(RecordVO::getRecordNumber).toList();

        Map<Long, List<Long>> tagMap = dao
                .selectTagsByRecordNumbers(ids).stream()
                .collect(Collectors.groupingBy(RecordTagVO::getRecordNumber,
                        Collectors.mapping(RecordTagVO::getUserNumber, Collectors.toList())));

        List<RecordDetailResponse> response = records.stream()
                .map(record -> new RecordDetailResponse(
                        record,
                        tagMap.getOrDefault(record.getRecordNumber(), new ArrayList<>())
                ))
                .toList();

        return response;
    }

    @Override
    @Transactional
    public RecordDetailResponse selectRecordDetail(Long recordNumber) {
        RecordVO record = dao.selectRecordDetail(recordNumber);
        if (record == null) {
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
    public boolean updateRecord(Long recordNumber, RecordRequest request, MultipartFile file) {
        RecordVO record = dao.selectRecordDetail(recordNumber);

        if (file != null && !file.isEmpty()) {
            if (record.getFileUrl() != null) {
                deleteFileFromS3(record.getFileUrl());
            }
            record.setFileUrl(uploadFile(file));
        }

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
    public boolean deleteRecord(Long recordNumber) {
        RecordVO record = dao.selectRecordDetail(recordNumber);
        if (record != null && record.getFileUrl() != null) {
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
            //throw new 커스텀 에러 뱉기;
            return "false";
        }
    }

    public void deleteFileFromS3(String fileUrl) {
        try {
            String bucket = dotenv.get("S3_BUCKET_NAME");
            // URL에서 파일명(Key) 추출 (보통 마지막 / 뒤가 파일명임)
            String key = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            amazonS3.deleteObject(bucket, key);
        } catch (Exception e) {
            // 파일 삭제 실패는 로그만 찍고 넘어가도 무방함 (핵심 로직은 아니니까)
            e.printStackTrace();
        }
    }

}
