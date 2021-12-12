package toyproject.runningmate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.user.User;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Builder
@ToString
public class UserDto {

    private Long id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    @JsonIgnore
    private Crew crew;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;

    public User toEntity(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .id(userDto.getId())
                .crew(userDto.getCrew())
                .nickName(userDto.getNickName())
                .regDate(userDto.getRegDate())
                .address(userDto.getAddress())
                .roles(userDto.getRoles())
                .isCrewLeader(userDto.isCrewLeader())
                .password(userDto.getPassword())
                .build();

        return user;
    }
}
