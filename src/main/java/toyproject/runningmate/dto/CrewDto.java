package toyproject.runningmate.dto;

import lombok.*;
import toyproject.runningmate.domain.crew.Crew;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CrewDto {

    private Long id;
    private Long crewLeaderId;
    private String crewRegion;
    private String openChat;
    private String crewName;
    private String explanation;
    private String image;
    private List<UserDto> userDtos = new ArrayList<>();
    private Set<String> requestUsers= new HashSet<>();

    @Builder
    public CrewDto(Crew crew) {
        id = crew.getId();
        crewLeaderId = crew.getCrewLeaderId();
        crewRegion = crew.getCrewRegion();
        openChat = crew.getOpenChat();
        crewName = crew.getCrewName();
        explanation = crew.getExplanation();
        image = crew.getImage();
    }
}