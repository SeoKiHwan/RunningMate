package toyproject.runningmate.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import toyproject.runningmate.domain.friend.FriendShip;
import toyproject.runningmate.domain.friend.FriendStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FriendShipDto {

    private String sendUser;
    private String receiveUser;
    private FriendStatus status;

    @Builder
    public FriendShipDto(String sendUser, String receiveUser, FriendStatus status) {
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.status = status;
    }

}

