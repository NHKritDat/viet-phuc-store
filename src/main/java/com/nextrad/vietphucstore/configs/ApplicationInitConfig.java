package com.nextrad.vietphucstore.configs;

import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import com.nextrad.vietphucstore.services.UserService;
import com.nextrad.vietphucstore.services.ViettelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationInitConfig {
    @Value("${MAIL_USERNAME}")
    private String adminEmail;

    @Bean
    public ApplicationRunner applicationRunner(UserService userService, ViettelService viettelService) {
        return args -> {
            if (!userService.isEmailExist(adminEmail))
                userService.createDefaultUser(adminEmail, UserRole.STAFF, UserStatus.VERIFIED);
            viettelService.setToken(viettelService.getAccessToken());
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
