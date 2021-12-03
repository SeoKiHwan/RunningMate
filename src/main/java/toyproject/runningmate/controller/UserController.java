package toyproject.runningmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.UserRepository;
import toyproject.runningmate.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
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
     * 크루 null 가능
     * 나머지는 not null
     * @param user
     * @return
     */
    //회원가입
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userService.join(user);
    }

    //로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userService.getUserByEmail(user.get("email"));

        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
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

        User member = userService.getUserByToken(request);

        UserDto userDto = userService.getUserDto(member);

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

        User deleteUser = userService.getUserByNickName(nickName);

        userRepository.deleteById(deleteUser.getId());

        return ResponseEntity.ok("삭제 완료");
    }

    /**
     *
     * FE에서 토큰, (변경할) nickName, address
     * BE에서 수정
     * @param request
     * @param user
     * @return
     */
    @Transactional
    @PostMapping("/user/{nickName}")
    public ResponseEntity<String> updateUser(HttpServletRequest request,  @RequestBody Map<String, String> user) {

        User member = userService.getUserByToken(request);

        if (userService.isExistNickName(user.get("nickName"))) {
            return new ResponseEntity<>("중복된 닉네임입니다.", HttpStatus.CONFLICT);
        }

        member.update(user.get("nickName"), user.get("address"));

        return ResponseEntity.ok("수정 완료");
    }

    /**
     * 3번 유저가 2번 유저 프로필 볼 때
     * FE에서 nickName(2번 유저), token(3번 유저, 현재 로그인 상태)
     * /user/{nickName}
     **/
    @GetMapping("/user/{nickName}")
    public Object getOtherUser(HttpServletRequest request, @RequestParam String nickName) {
        User member = userService.getUserByToken(request);

        if(member.getNickName().equals(nickName)){
            //같으면 mypage로 redirect
            return HttpStatus.FOUND;
        }

        User findUser = userService.getUserByNickName(nickName);
        UserDto findUserDto = userService.getUserDto(findUser);

        return findUserDto;
    }



    /**
     * 비밀번호 변경
     */

}