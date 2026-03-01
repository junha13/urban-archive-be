package archive.backend.domain.auth.service.lmpl;

import archive.backend.domain.auth.service.AuthService;
import archive.backend.domain.auth.service.UserVO;
import archive.backend.domain.auth.service.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDAO authDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    //private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, String> login(LoginRequest request) {
        UserVO user = authDAO.findByUserId(request.getId());

        if (user == null) {
            // custom Exception 뱉기
        }

        return Map.of("", "");
    }

}
