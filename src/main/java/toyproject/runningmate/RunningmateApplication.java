package toyproject.runningmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@SpringBootApplication
public class RunningmateApplication {

	@GetMapping("/")
	public String home() {
		return "Hello I'm your dfadfdfadfcvzv";
	}

	public static void main(String[] args) {

		SpringApplication.run(RunningmateApplication.class, args);
	}

}
