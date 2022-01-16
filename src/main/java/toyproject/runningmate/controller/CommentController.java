package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.dto.CommentDto;
import toyproject.runningmate.service.CommentService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    //댓글 생성
    @PostMapping("/boards/{board-id}/comments")
    public CommentDto save(HttpServletRequest request, @RequestBody String content,
                           @PathVariable("board-id") String boardId) {
        return commentService.saveComment(userService.getUserByToken(request), content,
                Long.parseLong(boardId));
    }

    //댓글 수정
    @PostMapping("/boards/comments/{comment-id}")
    public CommentDto update(@PathVariable("comment-id") String commentId, @RequestBody String content) {
        return commentService.updateComment(Long.parseLong(commentId), content);
    }

    //댓글 삭제
    @DeleteMapping("/boards/comments/{comment-id}")
    public ResponseEntity<String> delete(@PathVariable("comment-id") Long commentId) {
        commentService.deleteComment(commentId);

        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    //댓글 조회
    @GetMapping("/boards/{board-id}/comments")
    public ResponseEntity<List<CommentDto>> boardComments(@PathVariable("board-id") Long boardId) {
        List<CommentDto> boardComments = commentService.findBoardComments(boardId);

        return new ResponseEntity<>(boardComments, HttpStatus.OK);
    }
}
