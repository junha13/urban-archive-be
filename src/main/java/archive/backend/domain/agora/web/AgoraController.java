package archive.backend.domain.agora.web;

import archive.backend.domain.agora.service.AgoraService;
import archive.backend.domain.agora.service.dto.AgoraCommentCreateRequest;
import archive.backend.domain.agora.service.dto.AgoraPostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/api/agora")
@RestController
@RequiredArgsConstructor
public class AgoraController {

    private final AgoraService agoraService;

    @PostMapping("/post/insert")
    public ResponseEntity<?> insertPost(Authentication authentication, @RequestBody AgoraPostCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", agoraService.insertPost(authentication.getName(), hasAdminRole(authentication), request)));
    }

    @PutMapping("/post/update/{agoraNumber}")
    public ResponseEntity<?> updatePost(
            Authentication authentication,
            @PathVariable("agoraNumber") Long agoraNumber,
            @RequestBody AgoraPostCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", agoraService.updatePost(authentication.getName(), hasAdminRole(authentication), agoraNumber, request)));
    }

    @DeleteMapping("/post/delete/{agoraNumber}")
    public ResponseEntity<?> deletePost(Authentication authentication, @PathVariable("agoraNumber") Long agoraNumber) {
        return ResponseEntity.ok(Map.of("result", agoraService.deletePost(authentication.getName(), hasAdminRole(authentication), agoraNumber)));
    }

    @GetMapping("/post/list")
    public ResponseEntity<?> selectPostList() {
        return ResponseEntity.ok(Map.of("result", agoraService.selectPostList()));
    }

    @GetMapping("/post/{agoraNumber}")
    public ResponseEntity<?> selectPostDetail(@PathVariable("agoraNumber") Long agoraNumber) {
        return ResponseEntity.ok(Map.of("result", agoraService.selectPostDetail(agoraNumber)));
    }

    @PostMapping("/comment/insert/{agoraNumber}")
    public ResponseEntity<?> insertComment(
            Authentication authentication,
            @PathVariable("agoraNumber") Long agoraNumber,
            @RequestBody AgoraCommentCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", agoraService.insertComment(authentication.getName(), agoraNumber, request)));
    }

    @PutMapping("/comment/update/{agoraNumber}")
    public ResponseEntity<?> updateComment(
            Authentication authentication,
            @PathVariable("agoraNumber") Long agoraNumber,
            @RequestBody AgoraCommentCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", agoraService.updateComment(authentication.getName(), hasAdminRole(authentication), agoraNumber, request)));
    }

    @DeleteMapping("/comment/delete/{agoraNumber}")
    public ResponseEntity<?> deleteComment(Authentication authentication, @PathVariable("agoraNumber") Long agoraNumber) {
        return ResponseEntity.ok(Map.of("result", agoraService.deleteComment(authentication.getName(), hasAdminRole(authentication), agoraNumber)));
    }

    @PostMapping("/recommend/{agoraNumber}")
    public ResponseEntity<?> recommendAgora(Authentication authentication, @PathVariable("agoraNumber") Long agoraNumber) {
        return ResponseEntity.ok(Map.of("result", agoraService.recommendAgora(authentication.getName(), agoraNumber)));
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
