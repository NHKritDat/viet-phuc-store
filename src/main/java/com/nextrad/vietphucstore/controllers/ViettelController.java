package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.api.viettel.GetServicesRequest;
import com.nextrad.vietphucstore.dtos.requests.api.viettel.PricingRequest;
import com.nextrad.vietphucstore.dtos.responses.api.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ProvinceResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ViettelPricingResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ViettelServicesResponse;
import com.nextrad.vietphucstore.services.ViettelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/viettel")
public class ViettelController {
    private final ViettelService viettelService;

    @GetMapping("/provinces")
    public ResponseEntity<ApiItemResponse<List<ProvinceResponse>>> getProvinces(
            @RequestParam(defaultValue = "-1") int id
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                viettelService.getProvinces(id),
                "Đây là danh sách các tỉnh/thành phố"
        ));
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiItemResponse<List<DistrictResponse>>> getDistricts(
            @RequestParam(defaultValue = "-1") int id
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                viettelService.getDistricts(id),
                "Đây là danh sách các quận/huyện"
        ));
    }

    @PostMapping("/pricing")
    public ResponseEntity<ApiItemResponse<ViettelPricingResponse>> getPricing(@RequestBody PricingRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                viettelService.getPricing(request),
                "Đây là thông tin phí vận chuyển"
        ));
    }

    @PostMapping("/services")
    public ResponseEntity<ApiItemResponse<List<ViettelServicesResponse>>> getOrderServices(
            @RequestBody GetServicesRequest request
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(
                viettelService.getOrderServices(request),
                "Đây là các dịch vụ giao hàng hiện hỗ trợ"
        ));
    }
}
