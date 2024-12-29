package com.nextrad.vietphucstore.configs;

import com.nextrad.vietphucstore.entities.user.User;
import com.nextrad.vietphucstore.enums.user.UserRole;
import com.nextrad.vietphucstore.enums.user.UserStatus;
import com.nextrad.vietphucstore.repositories.user.UserRepository;
import com.nextrad.vietphucstore.services.ViettelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationInitConfig {
    @Value("${MAIL_USERNAME}")
    private String adminEmail;

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository, ViettelService viettelService) {
        return args -> {
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User user = new User();
                user.setName("admin");
                user.setEmail(adminEmail);
                user.setRole(UserRole.STAFF);
                user.setStatus(UserStatus.VERIFIED);
                user.setCreatedBy(adminEmail);
                user.setUpdatedBy(adminEmail);
                userRepository.save(user);
            }
            viettelService.setToken(viettelService.getAccessToken());
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
