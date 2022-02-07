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
    private String image;

    @Builder
    public UserDto(User user) {
        id = user.getId();
        email = user.getEmail();
        nickName = user.getNickName();
        regDate = user.getRegDate();
        address = user.getAddress();
        roles = user.getRoles();
        isCrewLeader = user.isCrewLeader();
        image = user.getImage();

        if(user.getCrew() != null)
            crewName = user.getCrew().getCrewName();
    }
}
