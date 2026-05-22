package archive.backend.domain.board.service;

import archive.backend.domain.board.service.dto.BoardCommentCreateRequest;
import archive.backend.domain.board.service.dto.BoardDetailResponse;
import archive.backend.domain.board.service.dto.BoardPostCreateRequest;

import java.util.List;

public interface BoardService {

    boolean insertPost(String loginId, String boardType, BoardPostCreateRequest request);

    List<BoardVO> selectPostList(String boardType);

    BoardDetailResponse selectPostDetail(String boardType, Long boardNumber);

    boolean insertComment(String loginId, String boardType, Long boardNumber, BoardCommentCreateRequest request);

    int recommendBoard(String loginId, String boardType, Long boardNumber);
}
