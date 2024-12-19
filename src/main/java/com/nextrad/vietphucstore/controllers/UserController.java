package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiItemResponse<TokenResponse>> loginGoogle(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.login(request), null));
    }

    @PostMapping("/auth/access-token")
    public ResponseEntity<ApiItemResponse<TokenResponse>> getAccessToken(@RequestBody AuthRequest request) {
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

    @GetMapping("/auth/email/verify")
    public ResponseEntity<ApiItemResponse<Object>> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.verifyEmail(token)));
    }

    @PutMapping("/auth/password/forgot")
    public ResponseEntity<ApiItemResponse<Object>> forgotPassword(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.forgotPassword(request)));
    }

    @PutMapping("/auth/password/reset")
    public ResponseEntity<ApiItemResponse<Object>> resetPassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.resetPassword(request)));
    }

    @PutMapping("/auth/password/change")
    public ResponseEntity<ApiItemResponse<Object>> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.changePassword(request)));
    }
}
