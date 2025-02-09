package org.ltt204.identityservice.config;


import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.enums.Roles;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
public class ApplicationInitConfig {
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findFirstByUsername("admin").isEmpty()) {
                HashSet<String> roles = new HashSet<>();
                roles.add(Roles.ADMIN.name());

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles).build();

                userRepository.save(admin);

                log.warn("Admin user has been created with default password: admin, please change it");
            }
        };
    }
}
