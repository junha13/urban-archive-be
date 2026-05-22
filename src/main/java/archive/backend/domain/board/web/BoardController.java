package archive.backend.domain.board.web;

import archive.backend.domain.board.service.BoardService;
import archive.backend.domain.board.service.dto.BoardCommentCreateRequest;
import archive.backend.domain.board.service.dto.BoardPostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/board")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/{boardType}/post/insert")
    public ResponseEntity<?> insertPost(
            Authentication authentication,
            @PathVariable("boardType") String boardType,
            @RequestBody BoardPostCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", boardService.insertPost(authentication.getName(), boardType, request)));
    }

    @GetMapping("/{boardType}/post/list")
    public ResponseEntity<?> selectPostList(@PathVariable("boardType") String boardType) {
        return ResponseEntity.ok(Map.of("result", boardService.selectPostList(boardType)));
    }

    @GetMapping("/{boardType}/post/{boardNumber}")
    public ResponseEntity<?> selectPostDetail(
            @PathVariable("boardType") String boardType,
            @PathVariable("boardNumber") Long boardNumber) {
        return ResponseEntity.ok(Map.of("result", boardService.selectPostDetail(boardType, boardNumber)));
    }

    @PostMapping("/{boardType}/comment/insert/{boardNumber}")
    public ResponseEntity<?> insertComment(
            Authentication authentication,
            @PathVariable("boardType") String boardType,
            @PathVariable("boardNumber") Long boardNumber,
            @RequestBody BoardCommentCreateRequest request) {
        return ResponseEntity.ok(Map.of("result", boardService.insertComment(authentication.getName(), boardType, boardNumber, request)));
    }

    @PostMapping("/{boardType}/recommend/{boardNumber}")
    public ResponseEntity<?> recommendBoard(
            Authentication authentication,
            @PathVariable("boardType") String boardType,
            @PathVariable("boardNumber") Long boardNumber) {
        return ResponseEntity.ok(Map.of("result", boardService.recommendBoard(authentication.getName(), boardType, boardNumber)));
    }
}
