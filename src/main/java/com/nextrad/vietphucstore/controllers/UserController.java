package com.nextrad.vietphucstore.controllers;

import com.nextrad.vietphucstore.dtos.requests.pageable.PageableRequest;
import com.nextrad.vietphucstore.dtos.requests.user.*;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiItemResponse;
import com.nextrad.vietphucstore.dtos.responses.standard.ApiListItemResponse;
import com.nextrad.vietphucstore.dtos.responses.user.SearchUser;
import com.nextrad.vietphucstore.dtos.responses.user.TokenResponse;
import com.nextrad.vietphucstore.dtos.responses.user.UserDetail;
import com.nextrad.vietphucstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<ApiItemResponse<TokenResponse>> loginGoogle(@RequestBody LoginGoogle request) {
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

    @GetMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiListItemResponse<SearchUser>> getUsers(
            @RequestParam(defaultValue = "", required = false) String search,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "ASC", required = false) Sort.Direction direction,
            @RequestParam(defaultValue = "id", required = false) String... properties
    ) {
        Page<SearchUser> users = userService.getUsers(search, new PageableRequest(page - 1, size, direction, properties));
        return ResponseEntity.ok(new ApiListItemResponse<>(
                users.getContent(),
                users.getSize(),
                users.getNumber() + 1,
                users.getTotalElements(),
                users.getTotalPages(),
                null));
    }

    @GetMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<UserDetail>> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.getUser(id), null));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiItemResponse<UserDetail>> getCurrentUser() {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.getCurrentUser(), null));
    }

    @PostMapping("/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<UserDetail>> createUser(@RequestBody UserModifyRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.createUser(request), null));
    }

    @PutMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<UserDetail>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserModifyRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.updateUser(id, request), null));
    }

    @DeleteMapping("/{id}/staff")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<ApiItemResponse<Object>> deleteUser(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiItemResponse<>(null, userService.deleteUser(id)));
    }

    @PutMapping("/current")
    public ResponseEntity<ApiItemResponse<UserDetail>> updateProfile(@RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.updateProfile(request), null));
    }

    @PutMapping("/current/avatar")
    public ResponseEntity<ApiItemResponse<UserDetail>> updateAvatar(@RequestBody UpdateAvatarRequest request) {
        return ResponseEntity.ok(new ApiItemResponse<>(userService.updateAvatar(request), null));
    }
}
