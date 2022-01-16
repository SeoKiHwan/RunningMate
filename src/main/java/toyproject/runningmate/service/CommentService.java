package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.Board.Board;
import toyproject.runningmate.domain.comment.Comment;
import toyproject.runningmate.dto.CommentDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.BoardRepository;
import toyproject.runningmate.repository.CommentRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final EntityManager em;

    @Transactional
    public CommentDto saveComment(UserDto userDto, String content, Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물"));

        Comment comment = Comment.builder()
                .author(userDto.getNickName())
                .content(content)
                .regDate(LocalDateTime.now())
                .board(board)
                .build();

        commentRepository.save(comment);

        return comment.toCommentDto();
    }

    //댓글 수정
    @Transactional
    public CommentDto updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글"));

        comment.update(content);

        return comment.toCommentDto();
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
