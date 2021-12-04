package toyproject.runningmate.domain.crew;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
<<<<<<< HEAD
public class Crew {     // crew
=======
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Crew {
>>>>>>> ced1f6fefccd6efcc90eae7926e6dbf4abab98c2

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

}
