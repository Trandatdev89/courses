package com.project01.skillineserver.excepion;


import com.project01.skillineserver.dto.ApiResponse;
import com.project01.skillineserver.excepion.CustomException.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GolbalException {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> appException(AppException ex){
        ApiResponse apiResponse = ApiResponse.builder()
                .message(ex.getErrorCode().getMessage())
                .code(ex.getErrorCode().getCode())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getCode()).body(apiResponse);
    }


}