package archive.backend.domain.news.web;

import archive.backend.domain.news.service.NewsService;
import archive.backend.domain.news.service.dto.keywordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/news")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @RequestMapping("/category")
    public ResponseEntity<?> selectNewsByCategory(@RequestBody keywordRequest keyword) {
       return ResponseEntity.ok(Map.of("result",newsService.selectNewsByCategory(keyword.getCategory())));
    }

    @RequestMapping("/word")
    public ResponseEntity<?> selectNewsBySearchWord(@RequestBody keywordRequest keyword) {
        return ResponseEntity.ok(Map.of("result",newsService.selectNewsBySearchWord(keyword.getWord())));
    }
}
