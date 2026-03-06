package archive.backend.domain.news.service.impl;

import archive.backend.domain.news.service.KeywordVO;
import archive.backend.domain.news.service.NewsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsDAO {

    // 키워드 리스트
    public List<KeywordVO> findSearchKeyword();

    // 크롤링 결과 insert
    public int insertNews(NewsVO news);


}
