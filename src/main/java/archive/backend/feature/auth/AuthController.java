package archive.backend.feature.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    /**
     * 로그인
     * @param
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<?> login(String a) {

       return ResponseEntity
               .status(200)
               .header("api", "auth/login")
               .body(Map.of("data", "result"));
    }
}
