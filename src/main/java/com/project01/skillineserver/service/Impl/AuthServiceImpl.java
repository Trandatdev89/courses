package com.project01.skillineserver.service.Impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.project01.skillineserver.dto.reponse.AuthResponse;
import com.project01.skillineserver.dto.request.LoginRequest;
import com.project01.skillineserver.dto.request.RegisterRequest;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.entity.RoleEntity;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.TokenType;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.RoleRepository;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.AuthService;
import com.project01.skillineserver.service.EmailService;
import com.project01.skillineserver.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final RedisService redisService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        if (!(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }
        if (!user.isAuthenticate()) {
            throw new AppException(ErrorCode.VERIFY_ACCOUNT);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        return AuthResponse.builder()
                .authenticated(true)
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(securityUtil.generateToken(user, authentication, "ACCESS_TOKEN"))
                .refreshToken(securityUtil.generateToken(user, authentication, "REFRESH_TOKEN"))
                .build();
    }

    @Override
    public boolean introspect(TokenRequest tokenRequest, TokenType tokenType) {
        boolean check = false;
        try {
            securityUtil.verifyToken(tokenType.equals(TokenType.ACCESS_TOKEN)
                            ? tokenRequest.getAccessToken()
                            : tokenRequest.getRefreshToken(),
                    tokenType.toString());
            check = true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return check;
    }

    @Override
    public String refreshToken(TokenRequest tokenRequest) throws ParseException {
        boolean check = introspect(tokenRequest, TokenType.REFRESH_TOKEN);
        if (!check) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        SignedJWT signedJWT = SignedJWT.parse(tokenRequest.getRefreshToken());
        UserEntity user = userRepository.findById(Long.parseLong(signedJWT.getJWTClaimsSet().getSubject())).get();
        RoleEntity role = roleRepository.findById(user.getRole_id()).get();
        List<GrantedAuthority> authorities =List.of(new SimpleGrantedAuthority(role.getName()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);
        return securityUtil.generateToken(user, authentication, "ACCESS_TOKEN");
    }

    @Override
    public void createAccount(RegisterRequest registerDTO) {
//        if(userRepository.existsByUsername(registerDTO.getUsername())) {
//            throw new AppException(ErrorCode.USER_EXITSED);
//        }
//        UserEntity user = UserEntity.builder()
//                .username(registerDTO.getUsername())
//                .password(passwordEncoder.encode(registerDTO.getPassword()))
//                .address(registerDTO.getAddress())
//                .phone(registerDTO.getPhone())
//                .fullname(registerDTO.getFullname())
//                .email(registerDTO.getEmail())
//                .authenticate(false)
//                .build();
//        UserEntity userCreated = userRepository.save(user);
//
//        userRoleRepository.save(UserRoleEntity.builder()
//                .role_id(2l)
//                .user_id(userCreated.getId())
//                .build());
//
//        emailService.verifyAccount(VerifyAccountRequest.builder()
//                .token(UUID.randomUUID().toString())
//                .linkUrl(verifyAccountUrl)
//                .userId(userCreated.getId())
//                .email(userCreated.getEmail())
//                .build());
    }


    @Override
    public void verifyAccount(String token, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        if (user.getCreateAt().plusMillis(10).isBefore(Instant.now())) {
            userRepository.deleteById(userId);
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        user.setAuthenticate(true);
        userRepository.save(user);
    }

    @Override
    public void logout(String token) throws ParseException {
       SignedJWT signedJWT = SignedJWT.parse(token);
       String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
       redisService.saveData(tokenId,token);
    }

}
