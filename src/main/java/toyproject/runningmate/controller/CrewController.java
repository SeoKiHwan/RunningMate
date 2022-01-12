package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.CrewRepository;
import toyproject.runningmate.repository.UserRepository;
import toyproject.runningmate.service.CrewService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;
    private final UserService userService;

    //페이징
    @GetMapping("/crew/pages/{pageNum}")
    public List<CrewDto> getCrewDtoByPageRequest(@PathVariable("pageNum") Integer pageNum){
        System.out.println("pageNum.getClass() = " + pageNum.getClass());
        PageRequest pageRequest = PageRequest.of(pageNum,5);
        return crewService.findByPage(pageRequest);
    }


    //친구목록 있으면 추가

    // '크루 참여 여부'는 로그인시 주었던 정보로 이미 검증이 이루어진다.

    /**
     * FE : 토큰 + crew 정보
     * BE
     * 크루 생성
     * 크루 만든 유저 -> 크루 리더 등록
     */

    @PostMapping("/crew/new")
    public ResponseEntity createCrew(HttpServletRequest request, @RequestBody CrewDto crewDto) {

        UserDto findUserDto = userService.getUserByToken(request);

        if(userService.hasCrew(findUserDto.getNickName())){  // User의 크루가 이미 존재하는 경우
            return ResponseEntity.ok("이미 크루가 존재합니다.");
        }

        crewService.save(findUserDto,crewDto);         // 새 크루 저장
        userService.updateCrewLeaderStatus(findUserDto.getNickName());    // isCrewLeader 상태 변경

        return new ResponseEntity("크루 생성 완료", HttpStatus.OK);
    }

    /**
     * 크루 상세 페이지
     *
     * FE : 크루 네임 전달
     *
     * BE : 크루 이름을 통해 크루Dto 반환
     */
    @GetMapping("/crew/{crewName}")
    public CrewDto getCrewPage(@PathVariable("crewName") String crewName){
        CrewDto crewDto = crewService.getCrewByName(crewName);
        crewDto.setUserDtos(crewService.getCrewMembersByCrewName(crewName));
        crewDto.setRequestUsers(crewService.getRequestList(crewName));
        return crewDto;
    }

    /**
     * 크루 신청
     *
     * FE : 크루 이름과 신청할 유저(토큰)
     *
     * BE : 크루를 찾고 해당 크루에게 요청
     */
    @PostMapping("/crew/{crewName}")
    public Long registCrew(HttpServletRequest request, @PathVariable("crewName") String crewName) {
        UserDto findUserDto = userService.getUserByToken(request);

        if(userService.hasCrew(findUserDto.getNickName()))
            throw new IllegalArgumentException("이미 크루가 존재한다.");

        CrewDto crewDto = crewService.getCrewByName(crewName);

        Long requestId = crewService.saveRequest(findUserDto, crewDto);

        return requestId;
    }

    /**
     * 크루 신청 거절
     *
     * FE : 유저 이름
     *
     * BE : 요청 거절
     */
    @DeleteMapping("/crew/request/{nickName}")
    public ResponseEntity reject(@PathVariable("nickName") String nickName) {
        crewService.rejectUser(nickName);
        return ResponseEntity.ok("삭제 완료");
    }

    /**
     * 크루 신청 승낙
     *
     * FE : 유저 이름
     *
     * BE : 요청 수락
     *
     */
    @PostMapping("/crew/request/{nickName}")
    public ResponseEntity admit(@PathVariable("nickName") String nickName){

        crewService.admitUser(nickName);

        crewService.rejectUser(nickName);

        return ResponseEntity.ok("추가 완료");
    }

    /**
     * 크루 삭제
     *
     * FE : crew 이름
     *
     * BE : 크루를 삭제(soft delete)
     *
     */
    @DeleteMapping("/crew/{crewName}")
    public ResponseEntity deleteCrew(@PathVariable("crewName") String crewName) {
        crewService.deleteCrew(crewName);
        return ResponseEntity.ok("삭제 완료");
    }

    /**
     * 크루장 위임
     *
     * FE : 토큰(리더) , 크루장을 줄 username
     *
     * BE : 위임
     *xx
     */
    @PostMapping("/crew/edit/leader/{userName}")
    public ResponseEntity changeCrewLeader(HttpServletRequest request,@PathVariable("userName") String userName){

        UserDto leaderDto = userService.getUserByToken(request);
        crewService.changeCrewLeader(leaderDto.getNickName(),userName);

        return ResponseEntity.ok("위임 완료");
    }

    /**
     * 크루 이름 변경
     *
     * FE : 현재 크루 이름, 바꿀 크루 이름
     *
     * BE : 크루 이름 변경
     */
    @PostMapping("/crew/edit/name")
    public ResponseEntity changeCrewName(@RequestParam("crewName") String crewName,
                                         @RequestParam("newCrewName") String newName){

        crewService.changeCrewName(crewName, newName);

        return ResponseEntity.ok("이름 변경 완료");
    }


    /**
     * 크루원 추방 or 자진 탈퇴
     *
     * FE : 유저 이름
     *
     * BE : 해당 크루에서 유저 삭제
     */

    @PostMapping("/crew/edit/member")
    public ResponseEntity changeMember(@RequestParam("userName") String userName) {
        crewService.changeMember(userName);
        return ResponseEntity.ok("추방 완료");
    }

    // 크루탈퇴 : 크루원이 1명인데(크루장) 탈퇴를 누를 때
}