package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.requests.viettel.ShippingFee;
import com.nextrad.vietphucstore.dtos.responses.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.PricingResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.ProvinceResponse;

import java.util.List;

public interface ViettelService {
    List<ProvinceResponse> getProvinces(int id);

    List<DistrictResponse> getDistricts(int id);

    String getAccessToken();

    void setToken(String token);

    PricingResponse getShippingFee(ShippingFee shippingFee);
}
