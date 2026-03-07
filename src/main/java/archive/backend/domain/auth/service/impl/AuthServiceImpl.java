package archive.backend.domain.auth.service.impl;

import archive.backend.domain.auth.service.AuthService;
import archive.backend.domain.auth.service.UserVO;
import archive.backend.domain.auth.service.dto.JoinRequest;
import archive.backend.domain.auth.service.dto.LoginRequest;
import archive.backend.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDAO authDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> loginResult = new HashMap<String, Object>();
        UserVO user = authDAO.findByUserId(request.getLoginId());

        if (user == null) {
            // custom Exception 뱉기
        }

        if (!passwordEncoder.matches(request.getLoginPassword(), user.getLoginPassword())) {
            // custom Exception 뱉기
        }

        String accessToken = jwtTokenProvider.createToken(user.getLoginId(), user.getRole());

        // 비밀번호 지워서 보내기
        user.setLoginPassword(null);

        loginResult.put("accessToken", accessToken);
        loginResult.put("userInfo", user);

        return loginResult;
    }

    @Override
    @Transactional
    public boolean join(JoinRequest.JoinForm request) {
        String encodePassword = passwordEncoder.encode(request.getLoginPassword());

        UserVO user = new UserVO();
        user.setLoginId(request.getLoginId());
        user.setLoginPassword(request.getLoginPassword());
        user.setStudentId(request.getStudentId());
        user.setName(request.getName());
        user.setNickName(request.getNickName());
        user.setEmail(request.getEmail());
        user.setProfileImage(request.getProfileImage());
        user.setLab(request.getLab());
        user.setDescription(request.getDescription());

        authDAO.insertUser(user);
        return true;
    }

    @Override
    @Transactional
    public boolean idCheck(JoinRequest.IdCheck request) {
        return authDAO.existByLoginId(request.getLoginId());
    }

    @Override
    @Transactional
    public boolean emailCheck(JoinRequest.EmailCheck request) {
        return authDAO.existByLoginId(request.getEmail());
    }

}
