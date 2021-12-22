package toyproject.runningmate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import toyproject.runningmate.config.security.JwtTokenProvider;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.LoginDto;
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



    public UserDto getUserById(Long userDtoId){
        UserDto userDto = userRepository.findById(userDtoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toUserDto();

        return userDto;
    }


    //토큰에서 User 추출
    public UserDto getUserByToken(HttpServletRequest request) {

        String token = request.getHeader("X-AUTH-TOKEN");
        String userEmail = jwtTokenProvider.getUserPk(token);

        UserDto userDto = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toUserDto();

        return userDto;
    }

    //닉네임에서 User 얻기
    public UserDto getUserByNickName(String nickName) {
        UserDto userDto = userRepository.findByNickName(nickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toUserDto();

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
    public LoginDto getUserByEmail(String email) {
        LoginDto loginDto = userRepository.findByEmail(email).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원")).toLoginDto();

        return loginDto;
    }

    //수정
    public void updateUser(Long changedUser, UserDto changeUserDto) {
        User findUser = userRepository.findById(changedUser)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        findUser.update(changeUserDto.getNickName(), changeUserDto.getAddress());
    }


   //회원가입
    @Transactional
    public Long join(LoginDto loginDto) {
        User member = User.builder()
                .email(loginDto.getEmail())
                .password(passwordEncoder.encode(loginDto.getPassword()))
                .roles(Collections.singletonList("ROLE_USER")) // 최초 가입시 USER 로 설정
                .address(loginDto.getAddress())
                .nickName(loginDto.getNickName())
                .regDate(LocalDateTime.now())
                .build();

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("member.getId() = " + member.getId());

        userRepository.save(member);

        return member.getId();
    }

    @Transactional
    public void updateCrewLeaderStatus(Long userDtoId ){
        User findUser = userRepository.findById(userDtoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        findUser.setCrewLeader(!findUser.isCrewLeader());
    }

    public boolean hasCrew(UserDto userDto){
        User user =userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        if(user.getCrew() != null) return true;
        else return false;
    }

}
