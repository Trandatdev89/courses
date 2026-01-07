package com.project01.skillineserver.controller;

import com.project01.skillineserver.config.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping(value = "/api/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public ResponseEntity<?> test(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseEntity.ok().body(userDetail);
    }

}
