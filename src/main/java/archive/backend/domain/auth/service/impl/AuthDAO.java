package archive.backend.domain.auth.service.impl;

import archive.backend.domain.auth.service.UserVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthDAO {

    public UserVO findByUserId(String userId);
}
