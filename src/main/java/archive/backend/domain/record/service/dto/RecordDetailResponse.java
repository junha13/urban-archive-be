package archive.backend.domain.record.service.dto;

import archive.backend.domain.record.service.RecordVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetailResponse {

    private RecordVO record;
    private List<Long> taggedUserNumbers;
}
