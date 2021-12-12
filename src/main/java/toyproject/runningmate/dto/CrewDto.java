package toyproject.runningmate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CrewDto {

    private Long id;
    private Long crewLeaderId;
    private String crewRegion;
    private String openChat;
    private String crewName;
    private List<RequestUserToCrew> requests = new ArrayList<>();
    private List<UserDto> userDtos = new ArrayList<>();         // 그릇

}