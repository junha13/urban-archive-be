package archive.backend.domain.news.service.impl;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsDAO {

    public List<String> findSearchkeyword();


}
