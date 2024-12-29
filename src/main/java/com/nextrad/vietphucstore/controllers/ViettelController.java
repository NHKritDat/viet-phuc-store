package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.ProvinceResponse;
import com.nextrad.vietphucstore.services.ViettelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return ResponseEntity.ok(new ApiItemResponse<>(viettelService.getProvinces(id), null));
    }

    @GetMapping("/districts")
    public ResponseEntity<ApiItemResponse<List<DistrictResponse>>> getDistricts(
            @RequestParam(defaultValue = "-1") int id
    ) {
        return ResponseEntity.ok(new ApiItemResponse<>(viettelService.getDistricts(id), null));
    }
}
