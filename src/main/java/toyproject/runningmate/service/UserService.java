package toyproject.runningmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //Entity -> Dto 변환
    public UserDto getUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .crew(user.getCrew())
                .nickName(user.getNickName())
                .regdate(user.getRegDate())
                .address(user.getAddress())
                .roles(user.getRoles())
                .build();

        return userDto;
    }

    //토큰에서 User 추출
    public User getUserByToken(HttpServletRequest request) {

        String token = request.getHeader("X-AUTH-TOKEN");
        String userEmail = jwtTokenProvider.getUserPk(token);

        User member = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        return member;
    }

    //닉네임에서 User 얻기
    public User getUserByNickName(String nickName) {
        User member = userRepository.findByNickName(nickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        return member;
    }

    //중복 닉네임 확인
    public boolean isExistNickName(String nickName) {
        boolean present = userRepository.findByNickName(nickName).isPresent();

        return present;
    }

    //이메일에서 User 얻기
    public User getUserByEmail(String email) {
        User member = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        return member;
    }


   //회원가입
    public Long join(Map<String, String> user) {
        User member = User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .address(user.get("address"))
                .nickName(user.get("nickName"))
                .build();

        userRepository.save(member);

        return member.getId();
    }
}
