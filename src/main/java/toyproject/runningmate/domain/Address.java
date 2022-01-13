package toyproject.runningmate.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
public class Address {

    private String si;
    private String gu;
    private String dong;

    @Builder
    public Address(String si, String gu, String dong) {
        this.si = si;
        this.gu = gu;
        this.dong = dong;
    }

    protected Address() {
    }
}
