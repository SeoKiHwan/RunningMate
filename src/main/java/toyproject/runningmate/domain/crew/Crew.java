package toyproject.runningmate.domain.crew;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
public class Crew {

    @Id
    @GeneratedValue
    @Column(name = "CREW_ID")
    private Long id;

    @OneToMany(mappedBy = "crew")
    private List<User> users = new ArrayList<>();

    @Column(name = "CREW_LEADER_ID")
    private Long crewLeaderId;

    @Column(name = "CREW_REGION", nullable = false)
    private String crewRegion;

    @Column(name = "OPEN_CHAT", nullable = false)
    private String openChat;

    @Column(name = "CREW_NAME", nullable = false, unique = true)
    private String crewName;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RequestUserToCrew> requests = new ArrayList<>();

    @Builder
    public Crew(Long crewLeaderId, String crewRegion, String openChat, String crewName) {
        this.crewLeaderId = crewLeaderId;
        this.crewRegion = crewRegion;
        this.openChat = openChat;
        this.crewName = crewName;
    }

    public CrewDto toCrewDto() {
        return CrewDto.builder()
                .id(id)
                .crewLeaderId(crewLeaderId)
                .crewRegion(crewRegion)
                .openChat(openChat)
                .crewName(crewName)
                .build();
    }
    public List<UserDto> userEntityListToDtoList(){
        return users.stream()
                .map(User::toUserDto)
                .collect(Collectors.toList());
    }

}
