package com.project01.skillineserver.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {

    USER_NOT_FOUND(1001,"User not exits!", HttpStatus.NOT_FOUND),
    PASSWORD_WRONG(1002,"Password wrong!", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1003,"Token not valid!", HttpStatus.UNAUTHORIZED),
    VERIFY_ACCOUNT(1005,"Account is not enable! Please confirm active account by email",HttpStatus.UNAUTHORIZED),
    ACCOUNT_IS_LOGOUT(1006,"Account is logout",HttpStatus.UNAUTHORIZED),
    USER_EXITED(1007,"Account is exited",HttpStatus.BAD_REQUEST),
    LIST_ID_EMPTY(1008,"List category id empty",HttpStatus.BAD_REQUEST),
    LECTURE_NOT_FOUND(1009,"Lecture not found",HttpStatus.NOT_FOUND),
    COURSE_EMPTY(1010,"Course is not empty",HttpStatus.BAD_REQUEST),
    COURSE_NOT_FOUND(1011,"Course is not found",HttpStatus.NOT_FOUND),
    UNAUTHORIZATED(1012,"Account is not authentication",HttpStatus.UNAUTHORIZED),
    FOBIDEN(1013,"Account is not permisson access resource this",HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER(9999,"Server error",HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;
    private String message;
    private HttpStatus statusCode;

}
