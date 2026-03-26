package archive.backend.domain.record.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequest {

    private Long userNumber;
    private Long subjectNumber;
    private String title;
    private String description;
    private Integer grade;
    private String semester;
    private List<Long> taggedUserNumbers; // 유저 태그 목록
}
