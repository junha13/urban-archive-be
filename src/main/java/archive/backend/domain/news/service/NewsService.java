package archive.backend.domain.news.service;

import archive.backend.domain.auth.service.dto.LoginRequest;
import archive.backend.domain.news.service.dto.NewsListResponse;

import java.util.Map;

public interface NewsService {

    void insertNews(NewsListResponse newsList, KeywordVO keyword);



}
