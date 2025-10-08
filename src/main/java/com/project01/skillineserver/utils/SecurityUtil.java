package com.project01.skillineserver.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.SignedJWT;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.TokenType;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.service.Impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    @Autowired
    @Qualifier("accessTokenEncoder")
    @Lazy
    private JwtEncoder accessTokenEncoder;

    @Autowired
    @Qualifier("refreshTokenEncoder")
    @Lazy
    private JwtEncoder refreshTokenEncoder;

    @Value("${jwt.secretKeyAccess}")
    private String secretKeyAccess;

    @Value("${jwt.secretKeyRefresh}")
    private String secretKeyRefresh;

    @Value("${jwt.expirationAccess}")
    private long expirationAccess;

    @Value("${jwt.expirationRefresh}")
    private long expirationRefresh;

//    @Autowired
//    private RedisService redisService;

    public String generateToken(UserEntity user, Authentication authentication, String tokenType){
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet jwtClaimsSet = null;
        if(tokenType.equals(TokenType.ACCESS_TOKEN.toString())){
            jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(user.getId().toString())
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(expirationAccess, ChronoUnit.SECONDS))
                    .claim("scope", getAuthorities(authentication))
                    .claim("loginAt", LocalDateTime.now().toString())
                    .issuer(user.getUsername())
                    .id(UUID.randomUUID().toString())
                    .build();
            String token = accessTokenEncoder.encode(JwtEncoderParameters.from(jwsHeader,jwtClaimsSet)).getTokenValue().toString();
            return token;
        }else{
            jwtClaimsSet = JwtClaimsSet.builder()
                    .subject(user.getId().toString())
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plus(expirationRefresh,ChronoUnit.SECONDS))
                    .id(UUID.randomUUID().toString())
                    .build();
            String token = refreshTokenEncoder.encode(JwtEncoderParameters.from(jwsHeader,jwtClaimsSet)).getTokenValue().toString();
            return token;
        }
    }


    public SignedJWT verifyToken(String token, String tokenType) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(tokenType.equals(TokenType.ACCESS_TOKEN.toString())
                ? secretKey() : secretRefreshKey());
        boolean verified = signedJWT.verify(verifier);
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();

        if(!(verified && signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date()))){
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

//        if(redisService.existsKey(tokenId)){
//            throw new AppException(ErrorCode.ACCOUNT_IS_LOGOUT);
//        }
        return signedJWT;
    }

    public SecretKey secretKey() {
        byte[] bytes = Base64.from(secretKeyAccess).decode();
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, 0,bytes.length,MacAlgorithm.HS256.getName());
        return secretKeySpec;
    }

    public SecretKey secretRefreshKey() {
        byte[] bytes = Base64.from(secretKeyRefresh).decode();
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, 0,bytes.length,MacAlgorithm.HS256.getName());
        return secretKeySpec;
    }

    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(item->item.getAuthority().toString()).collect(Collectors.joining(" "));
    }
}