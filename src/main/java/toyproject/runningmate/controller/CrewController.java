package toyproject.runningmate.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
import toyproject.runningmate.service.CrewService;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CrewController {

    private final CrewService crewService;
    private final UserService userService;
    private final CrewRepository crewRepository;
    private final JwtTokenProvider jwtTokenProvider;

    //페이징
//    @GetMapping("/crew")

    //친구목록 있으면 추가


   // 이미 크루에 속해있는지 체크할 것
    @Transactional
    @PostMapping("/crew/new")
    public ResponseEntity createCrewForm(HttpServletRequest request, @RequestBody CrewDto crewDto) {

        UserDto findUserDto = userService.getUserByToken(request);

        if(findUserDto.getCrew() != null){
            //이미 크루가 존재 가입 X
            return new ResponseEntity("이미 크루가 존재합니다.", HttpStatus.CONFLICT);
        }

        Crew crew = crewDto.toEntity();
        crewService.save(crew);

        findUserDto.setCrewLeader(true);
        findUserDto.toEntity(findUserDto).addCrew(crew);

        return new ResponseEntity("크루 생성 완료", HttpStatus.OK);
    }

    //  크루 지역( 직접기입-위치기반) : 구까지 .

//    @PostMapping("/crew/new")
//    public String create(@RequestBody Map crewFormData){         // userId  + 크루명,지역,소개글  추가할 크루원
//
//        String email = (String) crewFormData.get("email");
//        String crewName = (String) crewFormData.get("crewName");
//        String location = (String) crewFormData.get("location");
//        String intro = (String) crewFormData.get("intro");
//        List crewList = (List) crewFormData.get("crewList");
//
//
//        // 크루생성 .. dto?
//        CrewDto crewDto = new CrewDto();
//
//
//        // 이제 이 회원은 크루에 속해있게 된다.
//
//        return "redirect:/";        // 크루 상세페이지로 리다이렉션
//    }

    @GetMapping("/crew/new/emailSearch")     // 추가할 크루 멤버 검색(이메일)
    public String crewSearchByEmail(@RequestBody String email){

        return "emailSearch";
    }
    @GetMapping("/crew/new/friendSearch")  // 추가할 크루 멤버 검색(친구목록)
    public String crewSearchByFriendList(){
        return "friendSearch";
    }

}