package com.nextrad.vietphucstore.services;

import com.nextrad.vietphucstore.dtos.responses.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.ProvinceResponse;

import java.util.List;

public interface ViettelService {
    List<ProvinceResponse> getProvinces(int id);

    List<DistrictResponse> getDistricts(int id);
}
