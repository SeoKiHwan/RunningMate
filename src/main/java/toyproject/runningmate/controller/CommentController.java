package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping("/board/run")
    public CommentDto save(HttpServletRequest request, @RequestBody String content,
                           @RequestParam("boardId") String boardId) {
        return commentService.saveComment(userService.getUserByToken(request), content,
                Long.parseLong(boardId));
    }
}
