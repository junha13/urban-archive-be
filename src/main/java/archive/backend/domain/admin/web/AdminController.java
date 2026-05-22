package archive.backend.domain.admin.web;

import archive.backend.domain.admin.service.AdminService;
import archive.backend.domain.auth.service.AuthService;
import archive.backend.domain.auth.service.dto.JoinRequest;
import archive.backend.domain.auth.service.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/roleCheck")
    public String roleCheck() {
        return "???대뱶誘?留욎븘";
    }

}
