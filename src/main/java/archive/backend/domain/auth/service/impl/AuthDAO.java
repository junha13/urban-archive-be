package archive.backend.domain.auth.service.impl;

import archive.backend.domain.auth.service.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthDAO {

    UserVO findByUserId(String userId);

    void insertUser(UserVO user);

    boolean existByLoginId(String loginId);

    boolean existByEmail(String email);
}
