package archive.backend.domain.record.service;

import archive.backend.domain.record.service.dto.RecordDetailResponse;
import archive.backend.domain.record.service.dto.RecordRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecordService {

    boolean insertRecord(RecordRequest record, MultipartFile file);

    List<RecordDetailResponse> selectRecordList();

    RecordDetailResponse selectRecordDetail(Long recordNumber);

    boolean updateRecord(Long recordNumber, RecordRequest record, MultipartFile file);

    boolean deleteRecord(Long recordNumber);

    String uploadFile(MultipartFile file);

    void deleteFileFromS3(String fileUrl);
}
