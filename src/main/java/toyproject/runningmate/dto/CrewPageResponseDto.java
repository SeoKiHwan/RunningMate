package toyproject.runningmate.dto;
import lombok.*;
import toyproject.runningmate.domain.user.User;


import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrewPageResponseDto {

    private String crewLeaderName;
    private String crewRegion;
    private String openChat;
    private String crewName;

    private List<UserDto> crewMembers = new ArrayList<>();
//    private List<UserDto> requestUserDtos = new ArrayList<>();

}
