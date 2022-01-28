package toyproject.runningmate.dto;

import lombok.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<UserDto> userDtos = new ArrayList<>();
    private List<String> requestUsers= new ArrayList<>();

    @Builder
    public CrewDto(Long id, Long crewLeaderId, String crewRegion, String openChat, String crewName, String explanation) {
        this.id = id;
        this.crewLeaderId = crewLeaderId;
        this.crewRegion = crewRegion;
        this.openChat = openChat;
        this.crewName = crewName;
        this.explanation = explanation;
    }
}