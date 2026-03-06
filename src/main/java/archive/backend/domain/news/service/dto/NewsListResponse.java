package archive.backend.domain.news.service.dto;

import archive.backend.domain.news.service.NewsVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsListResponse {

    // api 반환값이 items 로 와서 이름 맞춰줌
    List<NewsVO> items;
}
