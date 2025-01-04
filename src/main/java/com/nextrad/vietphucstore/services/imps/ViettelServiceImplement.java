package com.nextrad.vietphucstore.services.imps;

import com.nextrad.vietphucstore.dtos.requests.viettel.*;
import com.nextrad.vietphucstore.dtos.responses.viettel.*;
import com.nextrad.vietphucstore.services.ViettelService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    @Getter
    @Setter
    private String token;
    @Value("${VIETTEL_API_GET_LIST_PROVINCES}")
    private String viettelApiGetListProvinces;
    @Value("${VIETTEL_API_GET_LIST_DISTRICTS}")
    private String viettelApiGetListDistricts;
    @Value("${VIETTEL_API_SIGN_PARTNER}")
    private String viettelApiSignPartner;
    @Value("${VIETTEL_PARTNER_USERNAME}")
    private String viettelPartnerUsername;
    @Value("${VIETTEL_PARTNER_PASSWORD}")
    private String viettelPartnerPassword;
    @Value("${VIETTEL_API_GET_PRICING}")
    private String viettelApiGetPricing;
    @Value("${VIETTEL_PRICING_SENDER_PROVINCE}")
    private int viettelPricingSenderProvince;
    @Value("${VIETTEL_PRICING_SENDER_DISTRICT}")
    private int viettelPricingSenderDistrict;
    @Value("${VIETTEL_PRICING_PRODUCT_TYPE}")
    private String viettelPricingProductType;
    @Value("${VIETTEL_PRICING_NATIONAL_TYPE}")
    private int viettelPricingNationalType;
    @Value("${VIETTEL_API_GET_ALL_SERVICES}")
    private String viettelApiGetAllServices;
    @Value("${VIETTEL_PRICING_ORDER_SERVICE_ADD}")
    private String viettelPricingOrderServiceAdd;

    @Override
    public List<ProvinceResponse> getProvinces(int id) {
        ResponseEntity<ViettelDataResponse<List<ProvinceResponse>>> response = restTemplate.exchange(
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
        ResponseEntity<ViettelDataResponse<List<DistrictResponse>>> response = restTemplate.exchange(
                viettelApiGetListDistricts + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).data();
    }

    @Override
    public String getAccessToken() {
        ResponseEntity<ViettelDataResponse<ViettelLoginResponse>> response = restTemplate.exchange(
                viettelApiSignPartner,
                HttpMethod.POST,
                new HttpEntity<>(new ViettelLoginRequest(viettelPartnerUsername, viettelPartnerPassword)),
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).data().token();
    }

    @Override
    public ViettelPricingResponse getPricing(PricingRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());

        ResponseEntity<ViettelDataResponse<ViettelPricingResponse>> response = restTemplate.exchange(
                viettelApiGetPricing,
                HttpMethod.POST,
                new HttpEntity<>(new ViettelPricingRequest(
                        request.weight(),
                        request.price(),
                        request.moneyCollection(),
                        request.price() >= 3000000 ? viettelPricingOrderServiceAdd : "",
                        request.orderService(),
                        viettelPricingSenderProvince,
                        viettelPricingSenderDistrict,
                        request.receiverProvince(),
                        request.receiverDistrict(),
                        viettelPricingProductType,
                        viettelPricingNationalType
                ), headers),
                new ParameterizedTypeReference<>() {
                }
        );
        return Objects.requireNonNull(response.getBody()).data();
    }

    @Override
    public List<ViettelServicesResponse> getOrderServices(GetServicesRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getToken());

        ResponseEntity<List<ViettelServicesResponse>> response =
                restTemplate.exchange(
                        viettelApiGetAllServices,
                        HttpMethod.POST,
                        new HttpEntity<>(
                                new ViettelGetServicesRequest(
                                        viettelPricingSenderProvince,
                                        viettelPricingSenderDistrict,
                                        request.provinceId(),
                                        request.districtId(),
                                        viettelPricingProductType,
                                        request.weight(),
                                        request.price(),
                                        request.moneyCollection(),
                                        viettelPricingNationalType
                                ),
                                headers
                        ),
                        new ParameterizedTypeReference<>() {
                        }
                );
        return Objects.requireNonNull(response.getBody());
    }

}
