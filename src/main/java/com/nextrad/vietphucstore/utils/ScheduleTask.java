package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.services.UserService;
import com.nextrad.vietphucstore.services.ViettelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private final ViettelService viettelService;
    private final UserService userService;

    @Value("${ACCESS_TOKEN_EXP}")
    private long accessTokenExp;

    @Scheduled(cron = "0 0 0 * * *")
    public void getViettelToken() {
        viettelService.setToken(viettelService.getAccessToken());
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteUnverifiedUser() {
        userService.deleteUnverifiedUsers(accessTokenExp);
    }

//    @Scheduled(cron = "0 */30 * * * *")
//    public void deleteInvalidToken() {
//        userService.deleteInvalidToken();
//    }

}
