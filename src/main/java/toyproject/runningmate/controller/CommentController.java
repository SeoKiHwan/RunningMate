package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.dto.CommentDto;
import toyproject.runningmate.service.CommentService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;

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
}
