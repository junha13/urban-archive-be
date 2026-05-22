package archive.backend.domain.board.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCommentCreateRequest {

    private String content;
    private Long parentBoardNumber;
}
