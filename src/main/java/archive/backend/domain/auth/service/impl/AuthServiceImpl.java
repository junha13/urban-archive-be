package archive.backend.domain.auth.service.impl;

import archive.backend.domain.auth.service.AuthService;
import archive.backend.domain.auth.service.UserVO;
import archive.backend.domain.auth.service.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDAO authDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    //private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, String> login(LoginRequest request) {
        Map<String, String> token = new HashMap<String, String>();
        UserVO user = authDAO.findByUserId(request.getId());

        if (user == null) {
            // custom Exception 뱉기
        }

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // custom Exception 뱉기
        }

        token.put("accessToken", "");

        return token;
    }

}
