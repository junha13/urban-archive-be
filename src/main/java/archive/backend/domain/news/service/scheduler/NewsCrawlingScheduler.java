package archive.backend.domain.news.service.scheduler;

import archive.backend.domain.news.service.KeywordVO;
import archive.backend.domain.news.service.NewsService;
import archive.backend.domain.news.service.dto.NewsListResponse;
import archive.backend.domain.news.service.impl.NewsDAO;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NewsCrawlingScheduler {

    private final WebClient webClient;
    private final NewsService newService;
    private final NewsDAO newsDAO;

    private final Dotenv dotenv;

    @Scheduled(cron = "0 * * * * *")
    public void crawlingNews() {
        // 1. 검색어를 디비에서 가져오고
        List<KeywordVO> keywordList = new ArrayList<>();
        keywordList = newsDAO.findSearchKeyword();

        // 2. 검색어마다 api 를 쏴서 뉴스를 가져오고
        for (KeywordVO keyword : keywordList) {
            try {
                NewsListResponse NewsList = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("https")
                                .host("openapi.naver.com")
                                .path("v1/search/news.json")
                                .queryParam("query", keyword.getKeyword())
                                .queryParam("display", 100)
                                .queryParam("sort", "date")
                                .build())
                        .header("X-Naver-Client-Id", dotenv.get("NAVER_CLIENT_ID"))
                        .header("X-Naver-Client-Secret", dotenv.get("NAVER_CLIENT_SECRET"))
                        .retrieve()
                        .bodyToMono(NewsListResponse.class)
                        .block();

                System.out.println(NewsList.getItems().get(0).toString());
                newService.insertNews(NewsList, keyword);

            } catch (Exception e) {
                // throw new CustomException(NewsErrorCode.NEWS_NOT_FOUND);
                log.error("{} 키워드 크롤링 처리 중 오류 발생 : {}", keyword, e.getMessage());
            }
        }

    }
}
