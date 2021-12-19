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
public class UserDto {

    private Long id;
    private String email;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;

    private String crewName;

    @Builder
    public UserDto(Long id, String email, String nickName, LocalDateTime regDate, String address, List<String> roles, boolean isCrewLeader) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.regDate = regDate;
        this.address = address;
        this.roles = roles;
        this.isCrewLeader = isCrewLeader;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .nickName(nickName)
                .regDate(regDate)
                .address(address)
                .roles(roles)
                .isCrewLeader(isCrewLeader)
                .build();
    }


}
