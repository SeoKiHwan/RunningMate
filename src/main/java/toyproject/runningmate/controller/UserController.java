package toyproject.runningmate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.UserRepository;
import toyproject.runningmate.service.UserService;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserService userService;

    //회원가입
    @PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return userRepository.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .build()).getId();
    }

    //로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일"));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    @GetMapping("/user")
    public String hi() {
        return "hi user";
    }

    @GetMapping("/admin")
    public String hello() {
        return "hello admin";
    }
    
    @GetMapping("/mypage")
    public UserDto getMyPage(@RequestBody Map<String, String> user){
        User member = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        UserDto userDto = userService.getUserDto(member);

        return userDto;
    }
}