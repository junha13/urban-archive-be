package archive.backend.domain.news.service.impl;

import archive.backend.domain.news.service.KeywordVO;
import archive.backend.domain.news.service.NewsVO;
import archive.backend.domain.news.service.NewsService;
import archive.backend.domain.news.service.dto.NewsListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsDAO newsDAO;

    @Override
    @Transactional
    public void insertNews(NewsListResponse newsList, KeywordVO keyword) {
        for (NewsVO news : newsList.getItems()) {
            news.setKeyword(keyword.getKeyword());
            news.setCategory(keyword.getCategory());
            int num = newsDAO.insertNews(news);
            if (num == 0) log.error("에러 원인 : {}", news);
        }
    }

}
