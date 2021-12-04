package toyproject.runningmate;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Collections;

@RestController
@SpringBootApplication
@RequiredArgsConstructor
public class RunningmateApplication {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@GetMapping("/")
	public String home() {
		return "Hello I'm your dfadfdfadfcvzv";
	}

	public static void main(String[] args) {

		SpringApplication.run(RunningmateApplication.class, args);
	}

	@PostConstruct
	public void init() {
		userRepository.save(User.builder()
				.email("email")
				.password(passwordEncoder.encode("password"))
				.roles(Collections.singletonList("ROLE_ADMIN"))
				.address("미정")
				.nickName("운영자")
				.build()).getId();
	}
}
