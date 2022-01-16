package toyproject.runningmate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter
public class CommentDto {

    private Long id;
    @JsonIgnore
    private BoardDto boardDto;
    private String content;
    private String author;
    private LocalDateTime regDate;

    @Builder
    public CommentDto(Long id, BoardDto boardDto, String content, String author, LocalDateTime regDate) {
        this.id = id;
        this.boardDto = boardDto;
        this.content = content;
        this.author = author;
        this.regDate = regDate;
    }

}
