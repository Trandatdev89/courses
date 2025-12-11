package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.reponse.AuthResponse;
import com.project01.skillineserver.dto.request.LoginRequest;
import com.project01.skillineserver.dto.request.RegisterRequest;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.enums.TokenType;
import com.project01.skillineserver.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ApiResponse<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.<AuthResponse>builder()
                .code(200)
                .message("Login Successful !")
                .data(authService.login(loginRequest))
                .build();
    }

    @PostMapping(value = "/introspect-token")
    public ApiResponse<Boolean> introspect(@RequestBody TokenRequest tokenRequest) {
        TokenType tokenType = tokenRequest.getTokenType();
        log.info("type token: {}", tokenType);
        return ApiResponse.<Boolean>builder()
                .code(200)
                .message("Token Valid!")
                .data(authService.introspect(tokenRequest, tokenType))
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody RegisterRequest registerDTO) {
        authService.createAccount(registerDTO);
        return ApiResponse.builder()
                .message("Hãy xác thực tài khoản bằng email")
                .code(200)
                .build();
    }

    @GetMapping(value = "/verify")
    public ApiResponse<?> verifyAccount(@RequestParam(name = "token") String token, @RequestParam(name = "userId") Long userId) {
        authService.verifyAccount(token, userId);
        return ApiResponse.builder()
                .message("Xác thực tài khoản thành công!")
                .code(200)
                .build();
    }

    @GetMapping(value = "/forgot-password")
    public ApiResponse<?> forgotPassword(@RequestParam(name = "email") String email) {
        authService.forgotPassword(email);
        return ApiResponse.builder()
                .message("Đổi mật khẩu thành công!")
                .code(200)
                .build();
    }

    @PostMapping(value = "/refresh-token")
    public ApiResponse<String> refreshToken(@RequestBody TokenRequest tokenRequest) throws ParseException {
        return ApiResponse.<String>builder()
                .code(200)
                .message("Refresh Token Success!")
                .data(authService.refreshToken(tokenRequest))
                .build();
    }

}
