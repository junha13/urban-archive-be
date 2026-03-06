package archive.backend.domain.news.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeywordVO {

    private Long keywordNumber;
    private String keyword;
    private String category;

}
