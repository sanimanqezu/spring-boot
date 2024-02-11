package za.co.example.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.example.persistance.repository.UserRepository;

import static za.co.example.constants.UsersConstant.users;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return (args -> userRepository.saveAll(users));
    }
}
