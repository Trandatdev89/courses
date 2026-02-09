package com.project01.skillineserver.controller;

import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.constants.AppConstants;
import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.ChangeEmailReq;
import com.project01.skillineserver.dto.request.ChangePasswordReq;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.service.AuthService;
import com.project01.skillineserver.service.Impl.AuthorizationService;
import com.project01.skillineserver.service.UserService;
import com.project01.skillineserver.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping(value = "/info")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ApiResponse<UserEntity> getMyInfo(@AuthenticationPrincipal CustomUserDetail customUserDetail) {

        Long userId = customUserDetail.getUser().getId();

        return ApiResponse.<UserEntity>builder()
                .message("get info success!")
                .code(200)
                .data(userService.getMyInfo(userId))
                .build();
    }

    @GetMapping(value = "/logout")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ApiResponse<?> logout(HttpServletRequest request) throws ParseException {
        String token = CookieUtil.getTokenFromCookie(AppConstants.ACCESS_TOKEN,request);
        authService.logout(token);
        return ApiResponse.builder()
                .message("Logout Success!")
                .code(200)
                .build();
    }

    @PostMapping(value = "/change-password")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ApiResponse<?> changePassword(@RequestBody ChangePasswordReq changePasswordReq,@AuthenticationPrincipal CustomUserDetail customUserDetail){
        userService.changePassword(changePasswordReq,customUserDetail.getUser().getId());
        return ApiResponse.builder()
                .message("Change Password Success!")
                .code(200)
                .build();
    }

    @PostMapping(value = "/change-email")
    @PreAuthorize("@authorizationService.isCanAccessApi()")
    public ApiResponse<?> changeEmail(@RequestBody ChangeEmailReq changeEmailReq,@AuthenticationPrincipal CustomUserDetail customUserDetail){
        System.out.println("🚨 CSRF ATTACK via FORM: Email changed to " + changeEmailReq.newEmail());
        userService.changeEmail(changeEmailReq.newEmail(),customUserDetail.getUser().getId());
        return ApiResponse.builder()
                .message("Change Mail Success!")
                .code(200)
                .build();
    }

}
