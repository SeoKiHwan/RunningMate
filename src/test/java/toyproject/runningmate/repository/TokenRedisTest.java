package toyproject.runningmate.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.dto.LoginDto;
import toyproject.runningmate.service.UserService;

@SpringBootTest
class TokenRedisTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    @Test
    void test() {


        // 유저 가입
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("email1");
        loginDto.setPassword("pw");
        loginDto.setNickName("nick");
        loginDto.setAddress("address");
        loginDto.setCrewLeader(false);
        loginDto.setImage("images");


        userService.join(loginDto);

        // 로그인 후 토큰
        String token = userService.login(loginDto).getToken();

        if (jwtTokenProvider.validateToken(token)) {
            System.out.println("유효한 토큰입니다");
        }

        //로그아웃 호출
        System.out.println(userService.logout(token));

        if (jwtTokenProvider.validateToken(token)) {
            System.out.println("아직 유효한 토큰");
        } else System.out.println("로그아웃 처리 된 토큰");

    }

}