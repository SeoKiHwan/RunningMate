package toyproject.runningmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //토큰에서 User 추출
    public UserDto getUserByToken(HttpServletRequest request) {

        String token = request.getHeader("X-AUTH-TOKEN");
        String userEmail = jwtTokenProvider.getUserPk(token);

        UserDto userDto = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toDto();

        userDto.setPassword("");

        return userDto;
    }

    //닉네임에서 User 얻기
    public UserDto getUserByNickName(String nickName) {
        UserDto userDto = userRepository.findByNickName(nickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toDto();

        userDto.setPassword("");

        return userDto;
    }

    @Transactional
    public void deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }

    //중복 닉네임 확인
    public boolean isExistNickName(String nickName) {
        boolean present = userRepository.findByNickName(nickName).isPresent();

        return present;
    }

    //이메일에서 User 얻기
    public UserDto getUserByEmail(String email) {
        UserDto userDto = userRepository.findByEmail(email).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toDto();

        return userDto;
    }

    //수정
    public void updateUser(Long changedUser, UserDto changeUserDto) {
        User findUser = userRepository.findById(changedUser)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        findUser.update(changeUserDto.getNickName(), changeUserDto.getAddress());
    }


   //회원가입
    @Transactional
    public Long join(UserDto userDto) {
        User member = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .address(userDto.getAddress())
                .nickName(userDto.getNickName())
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(member);

        return member.getId();
    }
}
