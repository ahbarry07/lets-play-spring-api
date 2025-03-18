package z01dakar.lets_play;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import z01dakar.lets_play.models.Role;
import z01dakar.lets_play.models.User;
import z01dakar.lets_play.repository.UserRepository;

@SpringBootApplication
public class LetsPlayApplication {

	public static void main(String[] args) {
		SpringApplication.run(LetsPlayApplication.class, args);
	}


//	BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
//
//	@Bean
//	CommandLineRunner dataLoader(final UserRepository userRepository) {
//		return new CommandLineRunner() {
//			@Override
//			public void run(String... args) throws Exception {
//				User admin = User.builder()
//								.name("Ahmad")
//								.email("ahmad@gmail.com")
//								.password(bCryptPasswordEncoder.encode("Ahmad123"))
//								.role(Role.ADMIN)
//								.build();
//
//				userRepository.save(admin);
//			}
//		};
//	}

	@Bean
	ModelMapper modelMapper() {
		return  new ModelMapper();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
