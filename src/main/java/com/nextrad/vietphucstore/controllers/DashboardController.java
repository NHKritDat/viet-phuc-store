package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.responses.api.dashboard.DashboardResponse;
import com.nextrad.vietphucstore.dtos.responses.api.standard.ApiItemResponse;
import com.nextrad.vietphucstore.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(new ApiItemResponse<>(
                dashboardService.getDashboardResponse(),
                "Đây là thông tin thống kê"
        ));
    }
}
