package com.nextrad.vietphucstore.utils;

import com.nextrad.vietphucstore.services.ViettelService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTask {
    private final ViettelService viettelService;

    @Scheduled(cron = "0 0 0 * * *")
    public void getViettelToken() {
        viettelService.setToken(viettelService.getAccessToken());
    }

}
