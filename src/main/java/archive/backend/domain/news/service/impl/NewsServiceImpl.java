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
            news.setKeywordList(keyword.getKeyword());
            news.setKeywordCategory(keyword.getCategory());
            int num = newsDAO.insertNews(news);
            if (num == 0) log.error("?먮윭 ?먯씤 : {}", news);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public NewsListResponse selectNewsList() {
        NewsListResponse response = new NewsListResponse();
        response.setItems(newsDAO.selectNewsList());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public NewsListResponse selectNewsByCategory(String category) {
        NewsListResponse response = new NewsListResponse();
        response.setItems(newsDAO.selectNewsByCategory(category));
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public NewsListResponse selectNewsBySearchWord(String word) {
        NewsListResponse response = new NewsListResponse();
        response.setItems(newsDAO.selectNewsBySearchWord(word));
        return response;
    }
}
