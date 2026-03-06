package archive.backend.domain.news.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class keywordRequest {

    private String category;
    private String word;
}
