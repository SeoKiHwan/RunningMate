package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.CrewPageResponseDto;
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
    private final int size = 5;

    //페이징
    @GetMapping("/crew/pages/{pageNum}")
    public List<CrewDto> getCrewDtoByPageRequest(@PathVariable("pageNum") Integer pageNum){
        System.out.println("pageNum.getClass() = " + pageNum.getClass());
        PageRequest pageRequest = PageRequest.of(pageNum,size);
        return crewService.findByPage(pageRequest);
    }

    /**
     Front -> Back
     Token , CrewDto
     */
    @Transactional
    @PostMapping("/crew/new")
    public ResponseEntity createCrew(HttpServletRequest request, @RequestBody CrewDto crewDto) {

        UserDto findUserDto = userService.getUserByToken(request);

        if(userService.hasCrew(findUserDto)){  // User의 크루가 이미 존재하는 경우
            return ResponseEntity.ok("이미 크루가 존재합니다.");
        }

        System.out.println("findUserDto = " + findUserDto);

        crewService.save(findUserDto,crewDto);         // 새 크루 저장
        userService.updateCrewLeaderStatus(findUserDto.getId());    // isCrewLeader 상태 변경

        return new ResponseEntity("크루 생성 완료", HttpStatus.OK);
    }

    // 크루 상세 페이지
    @GetMapping("/crew/{crewName}")
    public CrewDto getCrewPage(@RequestParam("crewName") String crewName){
        CrewDto crewDto = crewService.getCrewByName(crewName);
        crewDto.setUserDtos(crewService.getCrewMembersByCrewName(crewName));
        crewDto.setRequestUsers(crewService.getRequestList(crewName));
        return crewDto;
    }

    //크루 신청 버튼 눌렀을 때
    @PostMapping("/crew/{crewName}")
    public Long registCrew(HttpServletRequest request, @RequestParam String crewName) {
        UserDto findUserDto = userService.getUserByToken(request);

        if(userService.hasCrew(findUserDto))
            throw new IllegalArgumentException("이미 크루가 존재한다.");

        CrewDto crewDto = crewService.getCrewByName(crewName);

        Long requestId = crewService.saveRequest(findUserDto, crewDto);

        return requestId;
    }

    @DeleteMapping("/crew/request/{nickName}")
    public ResponseEntity reject(@RequestParam String nickName) {
        crewService.rejectUser(nickName);
        return ResponseEntity.ok("삭제 완료");
    }

    @PostMapping("/crew/request/{nickName}")
    public ResponseEntity admit(@RequestParam String nickName){
        crewService.addmitUser(nickName);
        crewService.rejectUser(nickName);
        return ResponseEntity.ok("추가 완료");
    }

    //크루 삭제
    //-> 크루에 요청 여러 개 만들고 나서 크루 삭제했을 때 리스트들이 삭제되늕니 확인
    @DeleteMapping("/crew/{crewName}")
    public ResponseEntity deleteCrew(@RequestParam String crewName) {
        crewService.deleteCrew(crewName);

        return ResponseEntity.ok("삭제 완료");
    }

    //위임
    /**
     * Crew
     * crewLeaderId
     *
     * User
     * 리더가 된 User는 isCrewLeader true
     * 리더안하는 User는 isCrewLeader false
     *
     */
    @PostMapping("/crew/edit/leader/{userName}")
    public ResponseEntity changeCrewLeader(HttpServletRequest request,@RequestParam String userName){

        UserDto leaderDto = userService.getUserByToken(request);
        crewService.changeCrewLeader(leaderDto.getNickName(),userName);

        return ResponseEntity.ok("위임 완료");
    }

    //크루 이름 변경
    @PostMapping("/crew/edit/crewname")
    public ResponseEntity changeCrewName(@RequestParam("crewName") String crewName,
                                         @RequestParam("newCrewName") String newName){

        crewService.changeCrewName(crewName, newName);

        return ResponseEntity.ok("이름 변경 완료");
    }

    //크루원 추방

    /**
     * 추방 버튼을 누르면
     * Crew
     * users(list)에서 삭제되야하고
     * User
     * crew를 null로,
     */

    @PostMapping("/crew/edit/member")
    public ResponseEntity changeMember(@RequestParam("userName") String userName) {
        crewService.changeMember(userName);
        return ResponseEntity.ok("추방 완료");
    }

    // 크루탈퇴 : 크루원이 1명인데(크루장) 탈퇴를 누를 때

    //요청 리스트 던져주는거 -> getRequestList

}