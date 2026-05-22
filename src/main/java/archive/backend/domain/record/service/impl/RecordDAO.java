package archive.backend.domain.record.service.impl;

import archive.backend.domain.record.service.RecordTagVO;
import archive.backend.domain.record.service.RecordVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RecordDAO {

    public int insertRecord(RecordVO record);

    List<RecordVO> selectRecordList();

    RecordVO selectRecordDetail(Long recordNumber);

    List<RecordTagVO> selectTagsByRecordNumbers(List<Long> recordNumbers);

    void insertUserTag(Long recordNumber,Long userNumber);

    List<Long> selectTaggedUserNumbers(Long recordNumber);

    void deleteTagsByRecordNumber(Long recordNumber);

    void updateRecord(RecordVO recordVO);

    void deleteRecord(Long recordNumber);

    Long findUserNumberByLoginId(String loginId);
}
