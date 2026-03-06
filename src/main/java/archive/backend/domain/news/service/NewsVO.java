package archive.backend.domain.news.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsVO {

    private Long newsNumber;
    private String title;
    private String link;
    private String description;
    private LocalDate uploadAt;

    @JsonFormat(pattern = "EEE, dd MMM yyyy HH:mm:ss Z", locale = "en")
    private ZonedDateTime pubDate;

}
