package toyproject.runningmate.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
public class CrewDto {

    private Long id;
    private Long crewLeaderId;
    private String crewRegion;
    private String openChat;
    private String crewName;
    private List<UserDto> userDtos = new ArrayList<>();         // 그릇

    @Builder
    public CrewDto(Long id, Long crewLeaderId, String crewRegion, String openChat, String crewName) {
        this.id = id;
        this.crewLeaderId = crewLeaderId;
        this.crewRegion = crewRegion;
        this.openChat = openChat;
        this.crewName = crewName;
    }

    public Crew toEntity(){
        return Crew.builder()
                .crewLeaderId(crewLeaderId)
                .crewRegion(crewRegion)
                .openChat(openChat)
                .crewName(crewName)
                .build();
    }
}