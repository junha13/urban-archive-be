package archive.backend.domain.board.service.impl;

import archive.backend.domain.board.service.BoardService;
import archive.backend.domain.board.service.BoardVO;
import archive.backend.domain.board.service.dto.BoardCommentCreateRequest;
import archive.backend.domain.board.service.dto.BoardCommentThreadResponse;
import archive.backend.domain.board.service.dto.BoardDetailResponse;
import archive.backend.domain.board.service.dto.BoardPostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private static final String POST_TYPE = "POST";
    private static final String COMMENT_TYPE = "COMMENT";
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final int COMMENT_DEPTH = 0;
    private static final int REPLY_DEPTH = 1;

    private final BoardDAO boardDAO;

    // Creates a new post inside the requested board type for any authenticated user.
    @Override
    @Transactional
    public boolean insertPost(String loginId, String boardType, BoardPostCreateRequest request) {
        validateBoardType(boardType);
        validatePostRequest(request);

        BoardVO board = new BoardVO();
        board.setBoardType(normalizeBoardType(boardType));
        board.setUserNumber(findUserNumberByLoginId(loginId));
        board.setTitle(request.getTitle().trim());
        board.setContent(request.getContent().trim());
        board.setNodeType(POST_TYPE);
        board.setDepth(COMMENT_DEPTH);
        board.setStatus(ACTIVE_STATUS);

        boardDAO.insertBoard(board);
        return true;
    }

    // Returns the latest post list for a single board type.
    @Override
    @Transactional(readOnly = true)
    public List<BoardVO> selectPostList(String boardType) {
        validateBoardType(boardType);
        return boardDAO.selectPostList(normalizeBoardType(boardType));
    }

    // Returns a post with its comments and replies grouped into one response.
    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse selectPostDetail(String boardType, Long boardNumber) {
        validateBoardType(boardType);

        BoardVO post = findBoardOrThrow(boardNumber);
        validateBoardTypeMatch(post, boardType);
        validatePostNode(post);

        List<BoardVO> comments = boardDAO.selectCommentsByRootBoardNumber(boardNumber);
        List<BoardCommentThreadResponse> threads = comments.stream()
                .filter(comment -> comment.getDepth() == COMMENT_DEPTH)
                .map(comment -> new BoardCommentThreadResponse(
                        comment,
                        comments.stream()
                                .filter(reply -> reply.getDepth() == REPLY_DEPTH)
                                .filter(reply -> Objects.equals(reply.getParentBoardNumber(), comment.getBoardNumber()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new BoardDetailResponse(post, threads);
    }

    // Creates either a comment or a reply while enforcing the maximum reply depth of one.
    @Override
    @Transactional
    public boolean insertComment(String loginId, String boardType, Long boardNumber, BoardCommentCreateRequest request) {
        validateBoardType(boardType);
        validateCommentRequest(request);

        BoardVO post = findBoardOrThrow(boardNumber);
        validateBoardTypeMatch(post, boardType);
        validatePostNode(post);

        BoardVO board = new BoardVO();
        board.setBoardType(post.getBoardType());
        board.setUserNumber(findUserNumberByLoginId(loginId));
        board.setRootBoardNumber(boardNumber);
        board.setContent(request.getContent().trim());
        board.setNodeType(COMMENT_TYPE);
        board.setStatus(ACTIVE_STATUS);

        if (request.getParentBoardNumber() == null) {
            board.setDepth(COMMENT_DEPTH);
        } else {
            BoardVO parentComment = findBoardOrThrow(request.getParentBoardNumber());
            validateReplyParent(boardNumber, post.getBoardType(), parentComment);
            board.setParentBoardNumber(parentComment.getBoardNumber());
            board.setDepth(REPLY_DEPTH);
        }

        boardDAO.insertBoard(board);
        return true;
    }

    // Applies a recommendation and blocks duplicate likes or self-like on comments.
    @Override
    @Transactional
    public int recommendBoard(String loginId, String boardType, Long boardNumber) {
        validateBoardType(boardType);

        Long userNumber = findUserNumberByLoginId(loginId);
        BoardVO target = findBoardOrThrow(boardNumber);
        validateBoardTypeMatch(target, boardType);

        if (COMMENT_TYPE.equals(target.getNodeType()) && Objects.equals(target.getUserNumber(), userNumber)) {
            throw new ResponseStatusException(BAD_REQUEST, "You cannot recommend your own comment.");
        }

        if (boardDAO.countBoardLike(boardNumber, userNumber) > 0) {
            throw new ResponseStatusException(CONFLICT, "You already recommended this item.");
        }

        boardDAO.insertBoardLike(boardNumber, userNumber);
        boardDAO.increaseLikeCount(boardNumber);
        return findBoardOrThrow(boardNumber).getLikeCount();
    }

    // Resolves the authenticated loginId into the persisted user number.
    private Long findUserNumberByLoginId(String loginId) {
        Long userNumber = boardDAO.findUserNumberByLoginId(loginId);
        if (userNumber == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "User information could not be resolved.");
        }
        return userNumber;
    }

    // Validates the required fields for a post request.
    private void validatePostRequest(BoardPostCreateRequest request) {
        if (request == null || isBlank(request.getTitle()) || isBlank(request.getContent())) {
            throw new ResponseStatusException(BAD_REQUEST, "Post title and content are required.");
        }
    }

    // Validates the required fields for a comment or reply request.
    private void validateCommentRequest(BoardCommentCreateRequest request) {
        if (request == null || isBlank(request.getContent())) {
            throw new ResponseStatusException(BAD_REQUEST, "Comment content is required.");
        }
    }

    // Loads a board row and fails when the row does not exist or is inactive.
    private BoardVO findBoardOrThrow(Long boardNumber) {
        BoardVO board = boardDAO.selectBoardByNumber(boardNumber);
        if (board == null || !ACTIVE_STATUS.equals(board.getStatus())) {
            throw new ResponseStatusException(NOT_FOUND, "Board item not found.");
        }
        return board;
    }

    // Ensures that the selected row is a post before a post-only operation proceeds.
    private void validatePostNode(BoardVO board) {
        if (!POST_TYPE.equals(board.getNodeType())) {
            throw new ResponseStatusException(BAD_REQUEST, "This operation is only available for posts.");
        }
    }

    // Ensures that the board type path value is present.
    private void validateBoardType(String boardType) {
        if (isBlank(boardType)) {
            throw new ResponseStatusException(BAD_REQUEST, "Board type is required.");
        }
    }

    // Confirms that the selected row belongs to the requested board type.
    private void validateBoardTypeMatch(BoardVO board, String boardType) {
        if (!Objects.equals(board.getBoardType(), normalizeBoardType(boardType))) {
            throw new ResponseStatusException(BAD_REQUEST, "This item does not belong to the requested board type.");
        }
    }

    // Validates that a reply targets a top-level comment within the same board post.
    private void validateReplyParent(Long boardNumber, String boardType, BoardVO parentComment) {
        if (!COMMENT_TYPE.equals(parentComment.getNodeType())) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies can only be added to comments.");
        }
        if (parentComment.getDepth() != COMMENT_DEPTH) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies cannot be nested deeper than one level.");
        }
        if (!Objects.equals(parentComment.getRootBoardNumber(), boardNumber)) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies must belong to the same post.");
        }
        if (!Objects.equals(parentComment.getBoardType(), boardType)) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies must belong to the same board type.");
        }
    }

    // Normalizes a board type string for consistent storage and lookup.
    private String normalizeBoardType(String boardType) {
        return boardType.trim().toUpperCase();
    }

    // Returns whether a string is null, empty, or only whitespace.
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
