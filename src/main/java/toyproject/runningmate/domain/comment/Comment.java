package toyproject.runningmate.domain.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.Board.Board;
import toyproject.runningmate.dto.CommentDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime regDate;
    private String author;

    @Builder
    public Comment(Board board, String content, LocalDateTime regDate, String author) {
        this.board = board;
        this.content = content;
        this.regDate = regDate;
        this.author = author;
    }

    //댓글 수정
    public void update(String content) {
        this.content = content;
    }

    public CommentDto toCommentDto() {
        return CommentDto.builder()
                .id(getId())
                .boardDto(getBoard().toBoardDto())
                .content(getContent())
                .regDate(getRegDate())
                .author(getAuthor())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) &&
                Objects.equals(getBoard(), comment.getBoard()) &&
                Objects.equals(getContent(), comment.getContent()) &&
                Objects.equals(getRegDate(), comment.getRegDate()) &&
                Objects.equals(getAuthor(), comment.getAuthor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBoard(), getContent(), getRegDate(), getAuthor());
    }
}
