package archive.backend.domain.record.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVO {

    private Long recordNumber;
    private Long userNumber;
    private String type;
    private Long subjectNumber;
    private String title;
    private String description;
    private String fileUrl;
    private Integer grade;
    private String semester;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
