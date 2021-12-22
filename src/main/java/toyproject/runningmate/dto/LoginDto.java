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
public class LoginDto {

    private Long id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;

    @Builder
    public LoginDto(Long id, String email, String password, String nickName, LocalDateTime regDate, String address, List<String> roles, boolean isCrewLeader) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.regDate = regDate;
        this.address = address;
        this.roles = roles;
        this.isCrewLeader = isCrewLeader;
    }

    public UserDto loginDtoToUserDto() {
        UserDto userDto = UserDto.builder()
                .email(getEmail())
                .id(getId())
                .nickName(getNickName())
                .regDate(getRegDate())
                .address(getAddress())
                .roles(getRoles())
                .isCrewLeader(isCrewLeader())
                .build();
            return userDto;
    }
}

