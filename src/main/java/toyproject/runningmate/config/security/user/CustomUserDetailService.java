package toyproject.runningmate.config.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toyproject.runningmate.domain.User.UserRepository;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    //토큰에 저장된 유저 정보를 활용해야 하기 때문에..
    //UserDetailsService를 상속받아 재정의 하는 과정

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailService.loadUserByUsername");
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없다."));
    }
}