package com.project01.skillineserver.controller;

import com.project01.skillineserver.config.CustomUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping(value = "/api/test")
@RestController
@Slf4j
public class TestController {

    @GetMapping
    public ResponseEntity<?> test(@AuthenticationPrincipal CustomUserDetail user){
        log.info("User Detail : {}",user);
        Principal principal = (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok().body(principal);
    }
}
