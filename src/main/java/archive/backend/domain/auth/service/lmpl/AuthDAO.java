package archive.backend.domain.auth.service.lmpl;

import archive.backend.domain.auth.service.UserVO;
import archive.backend.domain.auth.service.dto.LoginRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AuthDAO {

    public UserVO findByUserId(String userId);
}
