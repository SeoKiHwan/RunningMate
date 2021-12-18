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
// Password , Crew 필드 삭제
    private Long id;
    private String email;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
//                .password(password)
                .nickName(nickName)
                .regDate(regDate)
                .address(address)
                .roles(roles)
                .isCrewLeader(isCrewLeader)
                .build();
    }


}
