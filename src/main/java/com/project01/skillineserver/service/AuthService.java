package com.project01.skillineserver.service;

import com.project01.skillineserver.dto.reponse.AuthResponse;
import com.project01.skillineserver.dto.request.LoginRequest;
import com.project01.skillineserver.dto.request.RegisterRequest;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.enums.TokenType;

import java.text.ParseException;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    boolean introspect(TokenRequest tokenRequest, TokenType tokenType);
    String refreshToken(TokenRequest tokenRequest) throws ParseException;
    void createAccount(RegisterRequest registerRequest);
    void verifyAccount(String token,Long userId);
    void logout(String token) throws ParseException;
}
