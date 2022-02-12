package toyproject.runningmate.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.LoginDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenRedisRepositoryTest {

//    @Autowired
//    private TokenRedisRepository tokenRedisRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Test
    void test(){


        // 유저 가입
        LoginDto loginDto=new LoginDto();
        loginDto.setEmail("email1");
        loginDto.setPassword("pw");
        loginDto.setNickName("nick");
        loginDto.setAddress("address");
        loginDto.setCrewLeader(false);
        loginDto.setImage("images");


        userService.join(loginDto);

        // 로그인 후 토큰
        String token = userService.login(loginDto).getToken();

        // 로그아웃 했을 시






    }



}