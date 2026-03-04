package archive.backend.domain.news.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsVO {

    private long newsNumber;
    private String title;
    private String url;
    private String description;
    private LocalDate uploadAt;
    private LocalDate createAt;

}
