package archive.backend.domain.record.service.impl;

import archive.backend.domain.record.service.RecordTagVO;
import archive.backend.domain.record.service.RecordVO;
import archive.backend.domain.record.service.dto.RecordListSearchCondition;
import archive.backend.domain.record.service.dto.RecordReferenceSearchCondition;
import archive.backend.domain.record.service.dto.RecordSearchOptionResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecordDAO {

    int insertRecord(RecordVO record);

    List<RecordSearchOptionResponse> searchRecordReferences(RecordReferenceSearchCondition condition);

    List<RecordVO> selectRecordList(RecordListSearchCondition condition);

    RecordVO selectRecordDetail(Long recordNumber);

    List<RecordTagVO> selectTagsByRecordNumbers(List<Long> recordNumbers);

    void insertUserTag(Long recordNumber, Long userNumber);

    List<Long> selectTaggedUserNumbers(Long recordNumber);

    void deleteTagsByRecordNumber(Long recordNumber);

    void updateRecord(RecordVO recordVO);

    void deleteRecord(Long recordNumber);

    Long findUserNumberByLoginId(String loginId);
}
