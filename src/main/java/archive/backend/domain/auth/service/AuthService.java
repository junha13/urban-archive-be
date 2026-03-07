package archive.backend.domain.auth.service;

import archive.backend.domain.auth.service.dto.JoinRequest;
import archive.backend.domain.auth.service.dto.LoginRequest;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest request);

    boolean join(JoinRequest.JoinForm request);

    boolean idCheck(JoinRequest.IdCheck request);

    boolean emailCheck(JoinRequest.EmailCheck request);
}
