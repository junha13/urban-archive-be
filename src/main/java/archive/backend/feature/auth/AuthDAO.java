package archive.backend.feature.auth;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuthDAO {

    /**
     * 로그인
     */
    Map<String, Object> login(String a);
}
