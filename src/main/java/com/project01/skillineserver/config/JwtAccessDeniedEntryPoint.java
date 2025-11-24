package com.project01.skillineserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;


public class JwtAccessDeniedEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException {
        ErrorCode errorCode=ErrorCode.FOBIDEN;
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        ApiResponse<?> apiResponse=ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        ObjectMapper objectMapper=new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}