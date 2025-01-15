package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.api.viettel.GetServicesRequest;
import com.nextrad.vietphucstore.dtos.requests.api.viettel.PricingRequest;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ProvinceResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ViettelPricingResponse;
import com.nextrad.vietphucstore.dtos.responses.api.viettel.ViettelServicesResponse;

import java.util.List;

public interface ViettelService {
    List<ProvinceResponse> getProvinces(int id);

    List<DistrictResponse> getDistricts(int id);

    String getAccessToken();

    void setToken(String token);

    ViettelPricingResponse getPricing(PricingRequest request);

    List<ViettelServicesResponse> getOrderServices(GetServicesRequest request);
}
