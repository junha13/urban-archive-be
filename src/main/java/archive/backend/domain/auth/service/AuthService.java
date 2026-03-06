package archive.backend.domain.auth.service;

import archive.backend.domain.auth.service.dto.LoginRequest;

import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest request);

}
