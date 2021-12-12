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
public class CrewDto {

    private Long id;
    private Long crewLeaderId;
    private String crewRegion;
    private String openChat;
    private String crewName;
    private List<RequestUserToCrew> requests = new ArrayList<>();
    //수정
    private List<UserDto> userDtos = new ArrayList<>();         // 그릇


    @Builder
    public CrewDto(Long id, Long crewLeaderId, String crewRegion, String openChat, String crewName, List<RequestUserToCrew> requests, List<UserDto> userDtos) {
        this.id = id;
        this.crewLeaderId = crewLeaderId;
        this.crewRegion = crewRegion;
        this.openChat = openChat;
        this.crewName = crewName;
        this.requests = requests;
        this.userDtos= userDtos;      // **
    }

    public Crew toEntity(){         // Dto -> Entity
        return  Crew.builder()
                .id(id)
                .crewLeaderId(crewLeaderId)
                .openChat(openChat)
                .crewName(crewName)
                .crewRegion(crewRegion)
                .requests(requests)
//                .users(userDtos)
                .build();
    }


}