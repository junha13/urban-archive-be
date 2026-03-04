package archive.backend.domain.news.service.scheduler;

import archive.backend.domain.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsCrawlingSceduler {

    private final NewsService newService;

    @Scheduled(cron = "0 0 4 * * *")
    public void crawlingNews() {
        // 1. 검색어를 디비에서 가져오고

        // 2. 검색어마다 api 를 쏴서 뉴스를 가져오고

        // 3. 디비에 뉴스 정보 저장
    }
}
