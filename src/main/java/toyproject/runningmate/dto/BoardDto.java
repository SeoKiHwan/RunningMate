package toyproject.runningmate.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.runningmate.domain.Board.Board;
import toyproject.runningmate.domain.user.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private UserDto userDto;
    private String title;
    private String content;
    private boolean isClosed;
    private String meetingTime;
    private String meetingPlace;
    private LocalDateTime regDate;
    private int count;
    private String image;
    private String openChat;

    @Builder
    public BoardDto(Long id, UserDto userDto, String title, String content,boolean isClosed, String meetingTime, String meetingPlace, LocalDateTime regDate, int count, String image, String openChat) {
        this.id = id;
        this.userDto = userDto;
        this.title = title;
        this.content = content;
        this.isClosed = isClosed;
        this.meetingTime = meetingTime;
        this.meetingPlace = meetingPlace;
        this.regDate = regDate;
        this.count = count;
        this.image = image;
        this.openChat = openChat;
    }
}
