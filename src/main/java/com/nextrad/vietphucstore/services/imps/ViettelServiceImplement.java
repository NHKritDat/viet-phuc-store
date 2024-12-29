package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.responses.viettel.DataResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.DistrictResponse;
import com.nextrad.vietphucstore.dtos.responses.viettel.ProvinceResponse;
import com.nextrad.vietphucstore.services.ViettelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ViettelServiceImplement implements ViettelService {
    private final RestTemplate restTemplate;

    @Value("${VIETTEL_API_GET_LIST_PROVINCES}")
    private String viettelApiGetListProvinces;
    @Value("${VIETTEL_API_GET_LIST_DISTRICTS}")
    private String viettelApiGetListDistricts;

    @Override
    public List<ProvinceResponse> getProvinces(int id) {
        ResponseEntity<DataResponse<List<ProvinceResponse>>> response = restTemplate.exchange(
                viettelApiGetListProvinces + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).data();
    }

    @Override
    public List<DistrictResponse> getDistricts(int id) {
        ResponseEntity<DataResponse<List<DistrictResponse>>> response = restTemplate.exchange(
                viettelApiGetListDistricts + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).data();
    }

}
