package toyproject.runningmate.dto;

import lombok.*;
import toyproject.runningmate.domain.user.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class UserDto {
// Password , Crew 필드 삭제
    private Long id;
    private String email;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;
    private String token;
    private String crewName;

    @Builder
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.regDate = user.getRegDate();
        this.address = user.getAddress();
        this.roles = user.getRoles();
        this.isCrewLeader = user.isCrewLeader();

        if(user.getCrew() != null)
            this.crewName = user.getCrew().getCrewName();
    }
}
