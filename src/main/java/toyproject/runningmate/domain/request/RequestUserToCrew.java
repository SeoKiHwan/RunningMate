package toyproject.runningmate.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.crew.Crew;

import javax.persistence.*;

import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUserToCrew {

    @Id
    @GeneratedValue
    @Column(name = "REQUEST_ID")
    private Long id;

    @Column(name="NICKNAME")
    private String nickName;

    public RequestUserToCrew(String nickName) {
        this.nickName = nickName;
        System.out.println("this.getId() = " + this.getId());
    }

    


}
