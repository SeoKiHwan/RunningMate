package toyproject.runningmate.dto;

import lombok.*;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.user.User;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class CrewDto {


    private Long id;
    private List<User> users = new ArrayList<>();
    private Long crewLeaderId;
    private String crewRegion;
    private String openChat;
    private String crewName;

    @Builder
    public CrewDto(Long id, List<User> users, Long crewLeaderId, String crewRegion, String openChat, String crewName) {
        this.id = id;
        this.users = users;
        this.crewLeaderId = crewLeaderId;
        this.crewRegion = crewRegion;
        this.openChat = openChat;
        this.crewName = crewName;
    }

    public Crew toEntity(){
        return Crew.builder()
                .id(id)
                .users(users)
                .crewLeaderId(crewLeaderId)
                .openChat(openChat)
                .crewName(crewName)
                .crewRegion(crewRegion)
                .build();
    }
}