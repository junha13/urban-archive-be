package archive.backend.domain.auth.web;

import archive.backend.domain.auth.service.AuthService;
import archive.backend.domain.auth.service.dto.JoinRequest;
import archive.backend.domain.auth.service.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
       return ResponseEntity.ok(Map.of("result",service.login(request)));
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinRequest.JoinForm request) {
        return ResponseEntity.ok(Map.of("result",service.join(request)));
    }

    @PostMapping("/idCheck")
    public ResponseEntity<?> idCheck(@RequestBody JoinRequest.IdCheck request) {
        return ResponseEntity.ok(Map.of("result",service.idCheck(request)));
    }

    @PostMapping("/emailCheck")
    public ResponseEntity<?> emailCheck(@RequestBody JoinRequest.EmailCheck request) {
        return ResponseEntity.ok(Map.of("result",""));
    }
}
