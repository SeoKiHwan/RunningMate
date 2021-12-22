package toyproject.runningmate.domain.request;

import lombok.*;
import toyproject.runningmate.domain.crew.Crew;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserToCrew {

    @Id @NonNull
    @GeneratedValue
    @Column(name = "REQUEST_ID")
    private Long id;

    @Column(name="NICKNAME")
    private String nickName;

    @ManyToOne
    @JoinColumn(name = "CREW_ID")
    private Crew crew;

    public RequestUserToCrew(String nickName){
        this.nickName=nickName;
    }

    public void setCrew(Crew crew) {
        this.crew=crew;
        if(!crew.getRequests().contains(this)) crew.getRequests().add(this);
    }
}
