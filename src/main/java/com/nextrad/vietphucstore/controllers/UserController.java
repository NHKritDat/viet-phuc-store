package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.user.LoginPassword;
import com.nextrad.vietphucstore.dtos.requests.user.LogoutRequest;
import com.nextrad.vietphucstore.dtos.requests.user.RegisterRequest;
import com.nextrad.vietphucstore.dtos.requests.user.TokenRequest;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiItemResponse<TokenResponse>> login(@RequestBody LoginPassword request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.login(request), null));
    }

    @PostMapping("/auth/login/google")
    public ResponseEntity<ApiItemResponse<TokenResponse>> loginGoogle(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.login(request), null));
    }

    @PostMapping("/auth/access-token")
    public ResponseEntity<ApiItemResponse<TokenResponse>> getAccessToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.getAccessToken(request), null));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ApiItemResponse<Object>> logout(@RequestBody LogoutRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.logout(request)));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<ApiItemResponse<Object>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.register(request)));
    }
}
