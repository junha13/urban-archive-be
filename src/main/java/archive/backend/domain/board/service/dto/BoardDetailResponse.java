package archive.backend.domain.board.service.dto;

import archive.backend.domain.board.service.BoardVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponse {

    private BoardVO post;
    private List<BoardCommentThreadResponse> comments;
}
