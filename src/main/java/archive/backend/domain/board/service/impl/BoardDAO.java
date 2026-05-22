package archive.backend.domain.board.service.impl;

import archive.backend.domain.board.service.BoardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardDAO {

    void insertBoard(BoardVO board);

    List<BoardVO> selectPostList(String boardType);

    BoardVO selectBoardByNumber(Long boardNumber);

    List<BoardVO> selectCommentsByRootBoardNumber(Long rootBoardNumber);

    Long findUserNumberByLoginId(String loginId);

    int countBoardLike(@Param("boardNumber") Long boardNumber, @Param("userNumber") Long userNumber);

    void insertBoardLike(@Param("boardNumber") Long boardNumber, @Param("userNumber") Long userNumber);

    void increaseLikeCount(Long boardNumber);
}
