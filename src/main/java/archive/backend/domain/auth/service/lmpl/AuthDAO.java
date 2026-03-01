package archive.backend.domain.auth.service.lmpl;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuthDAO {

    Map<String, Object> login(String a);
}
