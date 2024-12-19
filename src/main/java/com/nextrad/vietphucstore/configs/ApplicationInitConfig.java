package com.nextrad.vietphucstore.configs;

import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitConfig {
    private static final String ADMIN_FULL_NAME = "admin";
    @Value("${MAIL_USERNAME}")
    private String adminEmail;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (!userRepository.existsByFullName(ADMIN_FULL_NAME)) {
                User user = new User();
                user.setFullName(ADMIN_FULL_NAME);
                user.setEmail(adminEmail);
                user.setAddress("HCM");
                user.setPhone("0123456789");
                user.setAvatar("");
                user.setRole(UserRole.STAFF);
                user.setStatus(UserStatus.VERIFIED);
                userRepository.save(user);
            }
        };
    }
}
