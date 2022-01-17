package toyproject.runningmate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {

    private String dou;
    private String si;
    private String gu;

    @Builder
    public Address(String dou, String si, String gu) {
        this.dou = dou;
        this.si = si;
        this.gu = gu;
    }



    protected Address() {
    }
}
