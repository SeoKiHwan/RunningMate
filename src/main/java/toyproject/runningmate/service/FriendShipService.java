package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.friend.FriendShip;
import toyproject.runningmate.domain.friend.FriendStatus;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.FriendShipDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.FriendShipRepository;
import toyproject.runningmate.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendShipService {

    private final UserService userService;
    private final FriendShipRepository friendShipRepository;


    // 친구 관계 확인
    public String getFriendShipRelation(String fromUser, String toUser){

        if(fromUser.equals(toUser)) return "ERROR";     // 자기 자신에게 보내는 경우
        userService.getUserByNickName(toUser);// 존재하는 유저에게 보냈는지 체크

        User fromUserEntity= userService.getUserEntity(fromUser);
        List<FriendShipDto> fromUserfriendShipDtos = fromUserEntity.userFriendShipListToDto();
        // userA의 friendShipList

        for (FriendShipDto fromUserfriendShipDto : fromUserfriendShipDtos) {
            if(fromUserfriendShipDto.getReceiveUser().equals(toUser)){
                return fromUserfriendShipDto.getStatus().toString(); // return SEND,RECEIVE,COMPLETED
            }
        }

        return "NOTHING";
    }

    // 친구 요청 보내기
    @Transactional
    public void sendFriendRequest(String fromUser, String toUser){
        // A:B SEND
        friendShipRepository.save(
                FriendShip.builder()
                .sendUser(userService.getUserEntity(fromUser))
                .receiveUserNickName(toUser)
                .status(FriendStatus.SEND)
                .build());

        // B:A RECEIVE
        friendShipRepository.save(
                FriendShip.builder()
                        .sendUser(userService.getUserEntity(toUser))
                        .receiveUserNickName(fromUser)
                        .status(FriendStatus.RECEIVE)
                        .build());

    }

    //친구 요청 취소
    @Transactional
    public void cancelFriendRequest(String fromUser, String toUser){
        User fromUserEntity = userService.getUserEntity(fromUser);
        User toUserEntity = userService.getUserEntity(toUser);

        List<FriendShip> fromUserFriendShipList = fromUserEntity.getFriendShipList();

        for (FriendShip friendShip : fromUserFriendShipList) {
            if(friendShip.getReceiveUserNickName().equals(toUser)){
                friendShipRepository.delete(friendShip);    // A:B SEND  삭제
            }
        }

        List<FriendShip> toUserFriendShipList = toUserEntity.getFriendShipList();

        for (FriendShip friendShip : toUserFriendShipList) {
            if(friendShip.getReceiveUserNickName().equals(fromUser)){
                friendShipRepository.delete(friendShip);   // B:A RECEIVE 삭제
            }
        }
    }


    // 친구 요청 수락
    // A:B COMPLETED      B:A COMPLETED  로 변경
    // JPQL(FriendShip 테이블) VS 객체 탐색(user->friendlist)
    @Transactional
    public void acceptFriendRequest(String fromUser, String toUser){
        User fromUserEntity = userService.getUserEntity(fromUser);
        User toUserEntity = userService.getUserEntity(toUser);

        List<FriendShip> fromUserFriendShipList = fromUserEntity.getFriendShipList();

        for (FriendShip friendShip : fromUserFriendShipList) {
            if(friendShip.getReceiveUserNickName().equals(toUser)){
                friendShip.changeStatus(FriendStatus.COMPLETED); // A:B COMPLETED
            }
        }

        List<FriendShip> toUserFriendShipList = toUserEntity.getFriendShipList();

        for (FriendShip friendShip : toUserFriendShipList) {
            if(friendShip.getReceiveUserNickName().equals(fromUser)){
                friendShip.changeStatus(FriendStatus.COMPLETED); // B:A COMPLETED
            }
        }
    }

    //친구 요청 거절



    //친구 삭제



}
