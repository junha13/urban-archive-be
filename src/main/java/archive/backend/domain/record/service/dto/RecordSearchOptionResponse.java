package archive.backend.domain.record.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordSearchOptionResponse {

    private Long number;
    private String label;
    private String subLabel;
    private String searchType;
}
