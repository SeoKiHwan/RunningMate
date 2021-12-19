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
}