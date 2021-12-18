package toyproject.runningmate.dto;




import lombok.*;
import toyproject.runningmate.domain.request.RequestUserToCrew;

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

    private List<UserDto> userDtos = new ArrayList<>();
    private List<RequestUserToCrew> requestUserToCrews = new ArrayList<>(); // 크루장만 확인가능




}
