package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/info")
    public ApiResponse<UserEntity> getMyInfo() {
        return ApiResponse.<UserEntity>builder()
                .message("get info success!")
                .code(200)
                .data(userService.getMyInfo())
                .build();
    }

}
