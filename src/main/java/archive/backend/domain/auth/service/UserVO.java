package archive.backend.domain.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private long userNumber;
    private String loginId;
    private String loginPassword;
    private String studentId;
    private String name;
    private String nickName;
    private String profileImage;
    private String lab; // 연구실
    private String description; // 자기소개
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;

    private String role;

}
