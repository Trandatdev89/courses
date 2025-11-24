package com.project01.skillineserver.excepion.CustomException;


import com.project01.skillineserver.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
}
