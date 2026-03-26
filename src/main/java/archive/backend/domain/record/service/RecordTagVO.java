package archive.backend.domain.record.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordTagVO {

    private Long tagNumber;
    private Long recordNumber;
    private Long userNumber;
}
