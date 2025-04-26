package org.ltt204.identityservice.infra.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaRole;
import org.ltt204.identityservice.infra.persist.jpa.entities.JpaUser;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaRoleRepository;
import org.ltt204.identityservice.infra.persist.jpa.repositories.JpaUserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApplicationInitConfig {
    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(
            JpaUserRepository userRepository,
            JpaRoleRepository jpaRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.findFirstByUsername("admin").isEmpty()) {
                jpaRoleRepository.save(JpaRole
                        .builder()
                        .name("ADMIN")
                        .description("Admin role")
                        .build());

                HashSet<JpaRole> roles = new HashSet<>();
                roles.add(jpaRoleRepository.findFirstByName("ADMIN"));

                JpaUser admin = JpaUser.builder()
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
