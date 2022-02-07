package toyproject.runningmate.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class LoginDto {

    private Long id;
    private String email;
    private String password;
    private String nickName;
    private LocalDateTime regDate;
    private String address;
    private List<String> roles = new ArrayList<>();
    private boolean isCrewLeader;
    private String image;
}

