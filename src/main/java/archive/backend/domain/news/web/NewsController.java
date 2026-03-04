package archive.backend.domain.news.web;

import archive.backend.domain.news.service.NewsService;
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

    private final NewsService service;

    @RequestMapping("/")
    public ResponseEntity<?> login() {

       return ResponseEntity.ok(Map.of("result",""));
    }
}
