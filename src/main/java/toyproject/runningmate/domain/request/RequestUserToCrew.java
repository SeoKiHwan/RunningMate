package toyproject.runningmate.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    }
}
