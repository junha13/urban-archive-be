package archive.backend.domain.board.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardVO {

    private Long boardNumber;
    private Long parentBoardNumber;
    private Long rootBoardNumber;
    private String boardType;
    private Long userNumber;
    private String title;
    private String content;
    private String nodeType;
    private Integer depth;
    private Integer likeCount;
    private Integer commentCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String loginId;
    private String name;
    private String role;
}
