package archive.backend.domain.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class JoinRequest {

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class JoinForm {
        private String loginId;
        private String loginPassword;
        private String studentId;
        private String name;
        private String nickName;
        private String profileImage;
        private String lab; // 연구실
        private String description; // 자기소개
        private String email;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class IdCheck {
        private String loginId;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @NoArgsConstructor
    public static class EmailCheck {
        private String email;
    }
}
