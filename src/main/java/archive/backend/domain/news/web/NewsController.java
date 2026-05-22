package archive.backend.domain.news.web;

import archive.backend.domain.news.service.NewsService;
import archive.backend.domain.news.service.dto.keywordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/news")
@RestController
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/list")
    public ResponseEntity<?> selectNewsList() {
        return ResponseEntity.ok(Map.of("result", newsService.selectNewsList()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> selectNewsByCategory(@PathVariable("category") String category) {
       return ResponseEntity.ok(Map.of("result", newsService.selectNewsByCategory(category)));
    }

    @GetMapping("/search")
    public ResponseEntity<?> selectNewsBySearchWord(@RequestParam("word") String word) {
        return ResponseEntity.ok(Map.of("result", newsService.selectNewsBySearchWord(word)));
    }

    @PostMapping("/category")
    public ResponseEntity<?> selectNewsByCategoryPost(@RequestBody keywordRequest keyword) {
        return ResponseEntity.ok(Map.of("result", newsService.selectNewsByCategory(keyword.getCategory())));
    }

    @PostMapping("/word")
    public ResponseEntity<?> selectNewsBySearchWordPost(@RequestBody keywordRequest keyword) {
        return ResponseEntity.ok(Map.of("result", newsService.selectNewsBySearchWord(keyword.getWord())));
    }
}
