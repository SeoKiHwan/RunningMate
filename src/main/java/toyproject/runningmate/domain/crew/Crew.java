package toyproject.runningmate.domain.crew;

import lombok.AllArgsConstructor;
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

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Crew {

    @Id
    @GeneratedValue
    @Column(name = "CREW_ID")
    private Long id;

    @OneToMany(mappedBy = "crew")
    private List<User> users = new ArrayList<>();

    @Column(name = "CREW_LEADER_ID")
    private Long crewLeaderId;

    @Column(name = "CREW_REGION")
    private String crewRegion;

    @Column(name = "OPEN_CHAT")
    private String openChat;

    @Column(name = "CREW_NAME")
    private String crewName;

    @OneToMany(mappedBy = "crew", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<RequestUserToCrew> requests = new ArrayList<>();

    public CrewDto toDto(){
        return CrewDto.builder()
                .id(id)
                .crewLeaderId(crewLeaderId)
                .crewRegion(crewRegion)
                .openChat(openChat)
                .crewName(crewName)
                .build();
    }


    // userEntityList -> userDtoList
    public List<UserDto> userEntityListToDtoList(){
        return users.stream()
                .map(User::toUserDto)
                .collect(Collectors.toList());
    }

    public void addRequest(RequestUserToCrew requestUserToCrew){
        this.requests.add(requestUserToCrew);
        if(requestUserToCrew.getCrew() != this) requestUserToCrew.setCrew(this);
    }


}
