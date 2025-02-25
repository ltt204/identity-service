package org.ltt204.identityservice.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.entity.Role;
import org.ltt204.identityservice.entity.User;
import org.ltt204.identityservice.repository.RoleRepository;
import org.ltt204.identityservice.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApplicationInitConfig {
    @Bean
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.findFirstByUsername("admin").isEmpty()) {
                roleRepository.save(Role
                        .builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .build());

                HashSet<Role> roles = new HashSet<>();
                roles.add(roleRepository.findFirstByName("ADMIN"));

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(admin);

                log.warn("Admin user has been created with default password: admin, please change it");
            }
        };
    }
}
