package toyproject.runningmate.domain.friend;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.FriendShipDto;
import toyproject.runningmate.dto.UserDto;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FriendShip {

    @Id
    @GeneratedValue
    @Column(name="friendship_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User sendUser;

    private String receiveUserNickName;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public FriendShipDto toFriendShipDto() {        // 받은요청/보낸요청/친구 리스트
        FriendShipDto friendShipDto = FriendShipDto.builder()
                .sendUser(sendUser.getNickName())
                .receiveUser(receiveUserNickName)
                .status(status)
                .build();
        return friendShipDto;
    }

    public void changeStatus(FriendStatus status){
        this.status=status;
    }

}
