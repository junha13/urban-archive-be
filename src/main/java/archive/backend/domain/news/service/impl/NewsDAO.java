package archive.backend.domain.news.service.impl;

import archive.backend.domain.news.service.KeywordVO;
import archive.backend.domain.news.service.NewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsDAO {

    // 키워드 리스트 조회
    List<KeywordVO> findSearchKeyword();

    // 크롤링 결과 저장
    int insertNews(NewsVO news);

    List<NewsVO> selectNewsList();

    List<NewsVO> selectNewsByCategory(@Param("category") String category);

    List<NewsVO> selectNewsBySearchWord(@Param("word") String word);
}
