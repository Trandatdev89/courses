package com.project01.skillineserver.controller;

import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.dto.request.TemplateMailReq;
import com.project01.skillineserver.service.TemplateMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/config")
public class SettingController {

    private final TemplateMailService templateMailService;

    @PostMapping(value = "/save-template-mail")
    public ApiResponse<?> saveTemplateMail(@RequestBody TemplateMailReq templateMailReq){
        templateMailService.saveTemplateMail(templateMailReq);
        return ApiResponse.builder()
                .message("Save template done")
                .code(200)
                .build();
    }
}
