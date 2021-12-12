package toyproject.runningmate.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.UserRepository;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     *
     * @param userDto
     * @return
     */
    //회원가입
    @PostMapping("/join")
    @Transactional
    public Long join(@RequestBody UserDto userDto) {
        return userService.join(userDto);
    }

    //로그인
    //패스워드 빼고 다 달라.
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserDto userDto) {

        UserDto findUserDto = userService.getUserByEmail(userDto.getEmail());

        if (!passwordEncoder.matches(userDto.getPassword(), findUserDto.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호");
        }

        String token = jwtTokenProvider.createToken(findUserDto.getEmail(), findUserDto.getRoles());

        findUserDto.setPassword("");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token);
        userInfo.put("userDto", findUserDto);

        return userInfo;
    }

    /**
     * 로그인 시
     * FE에서 토큰 주고
     * 유저 정보
     * @param request
     * @return
     */
    @GetMapping("/mypage")
    public UserDto getMyPage(HttpServletRequest request){
        UserDto userDto = userService.getUserByToken(request);
        return userDto;
    }

    /**
     * FE에서 닉네임을 줌
     * BE에서 닉네임으로 유저 찾고 삭제
     *
     * @param nickName
     * @return
     */
    @DeleteMapping("/user/{nickName}")
    public ResponseEntity<String> deleteUser(@RequestParam String nickName) {

        Long findUserId = userService.getUserByNickName(nickName).getId();

        userService.deleteUserById(findUserId);

        return ResponseEntity.ok("삭제 완료");
    }

    /**
     * 주소나 닉네임 변경
     *
     * FE에서 토큰, 변경할 nickName, address
     * BE에서 수정
     *
     * @param request
     * @param userDto
     * @return
     */
    @Transactional
    @PostMapping("/user/{nickName}")
    public ResponseEntity<String> updateUser(HttpServletRequest request,  @RequestBody UserDto userDto) {

        //바꿀 대상
        UserDto findUserDto = userService.getUserByToken(request);

        if (userService.isExistNickName(userDto.getNickName())){
            return new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.CONFLICT);
        }
        //바꿀 값
        userService.updateUser(findUserDto.getId(), userDto);

        return ResponseEntity.ok("수정 완료");
    }

    /**
     * 다른 사람 or 자기 자신의 프로필을 눌러 정보 조회할 경우
     *
     * 3번 유저가 2번 유저 프로필 볼 때 -> 2번 유저 정보 조회
     * 3번 유저가 3번 유저 프로필 볼 때 -> mypage로 리다이렉트
     *
     * FE에서 nickName(2번 유저), token(3번 유저, 현재 로그인 상태)
     * BE에선 닉네임을 비교하여 같으면 mypage, 다르면 현재 선택한 정보 조회
     **/
    @GetMapping("/user/{nickName}")
    public Object getOtherUser(HttpServletRequest request, @RequestParam String nickName) {
        UserDto userDto = userService.getUserByToken(request);

        if(userDto.getNickName().equals(nickName)){
            //같으면 mypage로 redirect
            return HttpStatus.FOUND;
        }

        UserDto findMemberDto = userService.getUserByNickName(nickName);

        return findMemberDto;
    }



    /**
     * 비밀번호 변경
     */

}