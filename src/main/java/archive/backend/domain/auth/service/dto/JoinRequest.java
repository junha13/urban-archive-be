package archive.backend.domain.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class JoinRequest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JoinForm {
        private String loginId;
        private String loginPassword;
        private String studentId;
        private String name;
        private String email;
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdCheck {
        private String loginId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailCheck {
        private String email;
    }
}
