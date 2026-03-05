package archive.backend.domain.news.service.scheduler;

import archive.backend.domain.news.service.NewsService;
import archive.backend.domain.news.service.impl.NewsDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsCrawlingSceduler {

    private final WebClient webClient;
    private final NewsService newService;
    private final NewsDAO newsDAO;

    @Scheduled(cron = "0 0 4 * * *")
    public void crawlingNews() {
        // 1. 검색어를 디비에서 가져오고
        List<String> keywordList = new ArrayList<>();
        keywordList = newsDAO.findSearchkeyword();

        // 2. 검색어마다 api 를 쏴서 뉴스를 가져오고
        for (String keyword : keywordList) {
            try {
                String result = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("openapi.naver.com")
                                .path("v1/search/news.json")
                                .queryParam("query", keyword)
                                .build())
                        .header("X-Naver-Client-Id", "")
                        .header("X-Naver-Client-Secret", "")
            }
        }
        // 3. 디비에 뉴스 정보 저장
    }
}
