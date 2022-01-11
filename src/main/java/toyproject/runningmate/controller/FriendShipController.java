package toyproject.runningmate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.domain.friend.FriendStatus;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.FriendShipDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.service.FriendShipService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class FriendShipController {

    private final UserService userService;
    private final FriendShipService friendShipService;

    // 친구목록
    @GetMapping("/user/friends")
    public ResponseEntity<List> getFriendList(HttpServletRequest request){
        UserDto userDto = userService.getUserByToken(request);  // 토큰으로 유저 찾기
        User userEntity = userService.getUserEntity(userDto.getNickName());

        List<FriendShipDto> friendShipDtos = userEntity.userFriendShipListToDto(); // 보낸요청/받은요청/친구 List

        List<String> friendList = new ArrayList<>();    // 친구목록 list

        for (FriendShipDto friendShipDto : friendShipDtos) {
            if(friendShipDto.getStatus()== FriendStatus.COMPLETED){ // 친구상태면
                friendList.add(friendShipDto.getReceiveUser());
            }
        }
        return ResponseEntity.ok().body(friendList);
    }

    // 친구 요청 받은 목록
    @GetMapping("/user/friend/receive")
    public ResponseEntity<List> getReceivedFriendRequestList(HttpServletRequest request){
        UserDto userDto = userService.getUserByToken(request);  // 토큰으로 유저 찾기
        User userEntity = userService.getUserEntity(userDto.getNickName());

        List<FriendShipDto> friendShipDtos = userEntity.userFriendShipListToDto(); // 보낸요청/받은요청/친구 List

        List<String> receiveList = new ArrayList<>();    // 친구목록 list

        for (FriendShipDto friendShipDto : friendShipDtos) {
            if(friendShipDto.getStatus()== FriendStatus.RECEIVE){ // 친구상태면
                receiveList.add(friendShipDto.getReceiveUser());
            }
        }
        return ResponseEntity.ok().body(receiveList);
    }

    //  친구 관계 확인 {SEND,RECEIVE,COMPLETED}
    //  토큰 던지는 유저 : URI 유저
    @GetMapping("/user/friend/relation/{nickName}")
    public ResponseEntity<String> getFriendShipRelation(HttpServletRequest request,
                                                        @PathVariable("nickName") String nickName) {
        UserDto tokenUserDto = userService.getUserByToken(request);

        if(!friendShipService.validateFriendShipRelation(tokenUserDto.getNickName(),nickName)){
            return ResponseEntity.ok().body("잘못된 요청"); // 예외처리
        }
        String relation = friendShipService.getFriendShipRelation(tokenUserDto.getNickName(), nickName);
        return ResponseEntity.ok().body(relation);
    }

    // (친구요청/친구수락/요청됨)버튼 눌렀을 때 -> 상태에 따라 기능 달라짐 / 구분 필요
    @PostMapping("/user/friend/button/{nickName}")
    public ResponseEntity<String> changeFriendShipStatus(HttpServletRequest request,
                                                         @PathVariable("nickName") String nickName) {

        UserDto tokenUserDto = userService.getUserByToken(request);

        if(!friendShipService.validateFriendShipRelation(tokenUserDto.getNickName(),nickName)){
            return ResponseEntity.ok().body("잘못된 요청"); // 예외처리
        }

        String relation = friendShipService.getFriendShipRelation(tokenUserDto.getNickName(), nickName);

        // NOTHING인 상황에서 버튼 누름 -> 친구 요청 보냄
        if(relation.equals("NOTHING")){
            friendShipService.sendFriendRequest(tokenUserDto.getNickName(), nickName);
            return ResponseEntity.ok().body("친구요청 성공");
        }

        // SEND인 상황에서 버튼 누름 -> 친구 요청 취소
        else if (relation.equals("SEND")) {
            deleteFriendShip(request, nickName);
            return ResponseEntity.ok().body("친구요청 취소 성공");
        }

        // RECEIVE인 상황에서 버튼 누름 -> 친구 요청 수락
        else if (relation.equals("RECEIVE")) {
            acceptFriendRequest(request,nickName);
            return ResponseEntity.ok().body("친구요청 수락 성공");
        }

        // COMPLETED인 상황에서 버튼 누름 -> 이벤트X
        else if (relation.equals("COMPLETED")) {
            return ResponseEntity.ok().body("이미 친구인 회원");
        }

        return ResponseEntity.ok().body("잘못된 요청"); // 예외처리
    }


    // 친구 요청 수락 ( 내 요청목록에서 수락)
    @PostMapping("/user/friend/accept/{nickName}")
    public ResponseEntity<String> acceptFriendRequest(HttpServletRequest request,
                                                         @PathVariable("nickName") String nickName) {

        UserDto tokenUserDto = userService.getUserByToken(request);

        if(!friendShipService.validateFriendShipRelation(tokenUserDto.getNickName(),nickName)){
            return ResponseEntity.ok().body("잘못된 요청"); // 예외처리
        }

        friendShipService.acceptFriendRequest(tokenUserDto.getNickName(),nickName);
        return ResponseEntity.ok().body("친구요청 수락 성공");

    }

    // 친구 요청 취소/ 친구 요청 거절 / 친구 삭제
    @DeleteMapping("/user/friend/delete/{nickName}")
    public ResponseEntity<String> deleteFriendShip(HttpServletRequest request,
                                                      @PathVariable("nickName") String nickName) {
        UserDto tokenUserDto = userService.getUserByToken(request);

        if(!friendShipService.validateFriendShipRelation(tokenUserDto.getNickName(),nickName)){
            return ResponseEntity.ok().body("잘못된 요청"); // 예외처리
        }

        friendShipService.deleteFriendShip(tokenUserDto.getNickName(),nickName);
        return ResponseEntity.ok().body("delete 성공");
    }


//    @DeleteMapping("/user/friend/allDelete")
//    public ResponseEntity<String> deleteUserAllFriendShip(HttpServletRequest request){
//        UserDto tokenUserDto = userService.getUserByToken(request);
//        friendShipService.deleteUserAllFriendShip(tokenUserDto.getNickName());
//        return ResponseEntity.ok().body("All delete 성공");
//    }



}
