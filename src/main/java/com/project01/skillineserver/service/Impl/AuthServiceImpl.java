package com.project01.skillineserver.service.Impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.constants.AppConstants;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
        UserEntity userInDB = user.getUser();


        if(!user.isAccountNonLocked()){
            if(isAccountStillLocked(userInDB)){
                throw new AppException(ErrorCode.ACCOUNT_IS_LOCKED);
            }
        }


        if (!(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))) {
            increaseAttemptLogin(userInDB);
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }

        if (!user.isEnabled()) {
            throw new AppException(ErrorCode.VERIFY_ACCOUNT);
        }

        if(isPasswordExpire(userInDB)){
                throw new AppException(ErrorCode.PASSWORD_IS_EXPIRED);
        }

        resetFailedAttempts(userInDB);

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
    public String refreshToken(TokenRequest tokenRequest, Authentication authentication) {
        log.info("type of token : {}", tokenRequest.getTokenType());
        boolean check = introspect(tokenRequest, tokenRequest.getTokenType());
        if (!check) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (!AuthenticationUtil.isAuthenticated(authentication)) {
            throw new AppException(ErrorCode.UNAUTHORIZATED);
        }

        return securityUtil.generateToken(Objects.requireNonNull(AuthenticationUtil.getUserDetail()), TokenType.ACCESS_TOKEN);
    }


    @Override
    public void createAccount(RegisterRequest registerDTO) {
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new AppException(ErrorCode.USER_EXITED);
        }

        UserEntity user = new UserEntity();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setAddress(registerDTO.getAddress());
        user.setEmail(registerDTO.getEmail());
        user.setFullname(registerDTO.getFullname());
        user.setPhone(registerDTO.getPhone());
        user.setRole(Role.USER);

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

    private void increaseAttemptLogin(UserEntity user) {
        Integer countLoginFail = user.getFailedLoginAttempts();

        if(countLoginFail==null){
            countLoginFail = 0;
        }

        countLoginFail++;

        if (countLoginFail >= AppConstants.MAX_FAILED_ATTEMPTS) {
            user.setLockTime(Instant.now());
            user.setLocked(false);
        } else {
            user.setFailedLoginAttempts(countLoginFail);
        }
        userRepository.save(user);
    }

    private void resetFailedAttempts(UserEntity user) {
        if (user.getFailedLoginAttempts() != null && user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }
    }

    private boolean isAccountStillLocked(UserEntity user) {
        boolean isCheck = true;

        if(user.getLockTime() == null){
            return isCheck;
        }

        Instant now = Instant.now();
        Instant unlockTime = user.getLockTime().plus(120, ChronoUnit.SECONDS);

        if (now.isAfter(unlockTime)) {
            user.setLocked(true);
            user.setLockTime(null);
            user.setFailedLoginAttempts(0);
            isCheck = false;
        }
        return isCheck;
    }

    private boolean isPasswordExpire(UserEntity user){
        Instant now = Instant.now();
        Instant lastTimeChangePassword = null;
        if ( user.getLastTimeChangePassword()!=null){
            lastTimeChangePassword = Instant.now();
        }
        if(now.isAfter(lastTimeChangePassword.plus(AppConstants.CHANGE_PASSWORD_PERIODIC,ChronoUnit.SECONDS))){
            user.setCredentialsNonExpired(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
