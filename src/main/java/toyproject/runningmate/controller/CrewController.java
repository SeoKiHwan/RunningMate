package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    //@GetMapping("/crew")

    //친구목록 있으면 추가

    // '크루 참여 여부'는 로그인시 주었던 정보로 이미 검증이 이루어진다.

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
        System.out.println("crewName = " + crewName);
        CrewDto crewDto = crewService.getCrewByName(crewName);
        crewDto.setUserDtos(crewService.getCrewMembersByCrewName(crewName));
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

    //요청 리스트 던져주는거 -> getRequestList

    //크루 삭제하면 요청도 같이 삭제되는지 확인

    //페이징

}