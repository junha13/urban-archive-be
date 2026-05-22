package archive.backend.domain.record.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordListSearchCondition {

    private String type;
    private String searchType;
    private String keyword;
}
