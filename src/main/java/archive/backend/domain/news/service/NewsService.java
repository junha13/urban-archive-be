package archive.backend.domain.news.service;

import archive.backend.domain.news.service.dto.NewsListResponse;

public interface NewsService {

    void insertNews(NewsListResponse newsList, KeywordVO keyword);

    NewsListResponse selectNewsList();

    NewsListResponse selectNewsByCategory(String category);

    NewsListResponse selectNewsBySearchWord(String word);
}
