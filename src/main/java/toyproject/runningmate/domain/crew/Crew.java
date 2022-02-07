package toyproject.runningmate.domain.crew;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;


import javax.persistence.*;
import java.util.*;
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

    @OneToMany(mappedBy = "crew")
    private Set<RequestUserToCrew> requests = new HashSet<>();


    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "D_flag")
    private boolean deleteFlag;

    private String image;

    @Builder
    public Crew(CrewDto crewDto) {
        crewLeaderId = crewDto.getCrewLeaderId();
        crewRegion = crewDto.getCrewRegion();
        openChat = crewDto.getOpenChat();
        crewName = crewDto.getCrewName();
        explanation = crewDto.getExplanation();
        image = crewDto.getImage();
    }

    public CrewDto toCrewDto() {
        return CrewDto.builder()
                .crew(this)
                .build();
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void changeCrewName(String crewName) {
        this.crewName = crewName;
    }

    public void changeCrewLeaderId(Long newLeaderId){
        this.crewLeaderId=newLeaderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return isDeleteFlag() == crew.isDeleteFlag() &&
                Objects.equals(getId(), crew.getId()) &&
                Objects.equals(getUsers(), crew.getUsers()) &&
                Objects.equals(getCrewLeaderId(), crew.getCrewLeaderId()) &&
                Objects.equals(getCrewRegion(), crew.getCrewRegion()) &&
                Objects.equals(getOpenChat(), crew.getOpenChat()) &&
                Objects.equals(getCrewName(), crew.getCrewName()) &&
                Objects.equals(getRequests(), crew.getRequests()) &&
                Objects.equals(getExplanation(), crew.getExplanation());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsers(), getCrewLeaderId(), getCrewRegion(), getOpenChat(), getCrewName(), getRequests(), getExplanation(), isDeleteFlag());
    }

}
