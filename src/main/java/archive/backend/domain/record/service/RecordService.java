package archive.backend.domain.record.service;

import archive.backend.domain.record.service.dto.RecordDetailResponse;
import archive.backend.domain.record.service.dto.RecordRequest;
import archive.backend.domain.record.service.dto.RecordSearchOptionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {

    boolean insertRecord(String loginId, RecordRequest record, MultipartFile file);

    List<RecordSearchOptionResponse> searchRecordReferences(String searchType, String keyword);

    List<RecordDetailResponse> selectRecordList(String type, String searchType, String keyword);

    RecordDetailResponse selectRecordDetail(Long recordNumber);

    boolean updateRecord(Long recordNumber, String loginId, boolean admin, RecordRequest record, MultipartFile file);

    boolean deleteRecord(Long recordNumber, String loginId, boolean admin);

    String uploadFile(MultipartFile file);

    void deleteFileFromS3(String fileUrl);
}
