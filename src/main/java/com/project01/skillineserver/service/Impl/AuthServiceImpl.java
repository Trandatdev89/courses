package com.project01.skillineserver.service.Impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.dto.reponse.AuthResponse;
import com.project01.skillineserver.dto.request.LoginRequest;
import com.project01.skillineserver.dto.request.RegisterRequest;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.dto.request.VerifyAccountRequest;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.Role;
import com.project01.skillineserver.enums.TokenType;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.AuthService;
import com.project01.skillineserver.service.EmailService;
import com.project01.skillineserver.utils.AuthenticationUtil;
import com.project01.skillineserver.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${url.verify-account}")
    private String verifyAccountUrl;

    private final UserDetailsService userDetailsService;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final RedisService redisService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        CustomUserDetail user = (CustomUserDetail) userDetailsService.loadUserByUsername(loginRequest.getUsername());
        if (!(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }
        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.VERIFY_ACCOUNT);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return AuthResponse.builder()
                .authenticated(true)
                .userId(user.getUser().getId())
                .username(user.getUsername())
                .accessToken(securityUtil.generateToken(Objects.requireNonNull(AuthenticationUtil.getUserDetail()), TokenType.ACCESS_TOKEN))
                .refreshToken(securityUtil.generateToken(Objects.requireNonNull(AuthenticationUtil.getUserDetail()), TokenType.REFRESH_TOKEN))
                .build();
    }

    @Override
    public boolean introspect(TokenRequest tokenRequest, TokenType tokenType) {
        boolean isTokenAccess = tokenType.equals(TokenType.ACCESS_TOKEN);
        boolean check = false;
        try {
            securityUtil.verifyToken(isTokenAccess
                            ? tokenRequest.getAccessToken()
                            : tokenRequest.getRefreshToken(),
                    tokenType);
            check = true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return check;
    }

    @Override
    public String refreshToken(TokenRequest tokenRequest) {
        boolean check = introspect(tokenRequest, TokenType.REFRESH_TOKEN);
        if (!check) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        return securityUtil.generateToken(Objects.requireNonNull(AuthenticationUtil.getUserDetail()), TokenType.REFRESH_TOKEN);
    }

    @Override
    public void createAccount(RegisterRequest registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new AppException(ErrorCode.USER_EXITED);
        }
        UserEntity user = UserEntity.builder()
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .phone(registerDTO.getPhone())
                .fullname(registerDTO.getFullname())
                .email(registerDTO.getEmail())
                .role(Role.USER)
                .build();
        UserEntity userCreated = userRepository.save(user);


        emailService.verifyAccount(VerifyAccountRequest.builder()
                .token(UUID.randomUUID().toString())
                .linkUrl(verifyAccountUrl)
                .userId(userCreated.getId())
                .email(userCreated.getEmail())
                .build());
    }


    @Override
    public void verifyAccount(String token, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getCreateAt().plus(10, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            userRepository.deleteById(userId);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        user.setDisable(true);
        userRepository.save(user);
    }

    @Override
    public void logout(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
        redisService.saveData(tokenId, token);
    }

}
