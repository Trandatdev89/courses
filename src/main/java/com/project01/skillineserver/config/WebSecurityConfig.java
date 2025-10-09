package com.project01.skillineserver.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.project01.skillineserver.dto.request.TokenRequest;
import com.project01.skillineserver.entity.RoleEntity;
import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.enums.TokenType;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.RoleRepository;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.AuthService;
import com.project01.skillineserver.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private static final String[] PUBLIC_ENTRYPOINT = {"/auth/**", "/file/**", "/chat/**",
            "/vnpay-payment/**","/api/lecture/**","/api/course/**"};

    @Lazy
    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private AuthService authService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserEntity userEntity = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
            RoleEntity role = roleRepository.findById(userEntity.getRole_id()).get();
            List<GrantedAuthority> authorities =List.of(new SimpleGrantedAuthority(role.getName()));
            return new CustomUserDetail(userEntity, authorities);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(http -> http
                        .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/review/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/image/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/uploads/lecture/**").permitAll()
                        .requestMatchers(PUBLIC_ENTRYPOINT).permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth -> oauth
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                        .accessDeniedHandler(new JwtAccessDeniedEntryPoint())
                        .jwt(config -> config
                                .decoder(accessTokenDecoder())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    @Qualifier("accessTokenEncoder")
    public JwtEncoder accessTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(securityUtil.secretKey()));
    }

    @Bean
    @Qualifier("accessTokenDecoder")
    public JwtDecoder accessTokenDecoder() {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(securityUtil.secretKey()).macAlgorithm(MacAlgorithm.HS256).build();
        return token -> {
            try {
                var responseToken = authService.introspect(TokenRequest.builder().accessToken(token).build(), TokenType.ACCESS_TOKEN);
                if (!responseToken) {
                    throw new AppException(ErrorCode.INVALID_TOKEN);
                }
                return nimbusJwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    @Qualifier("refreshTokenEncoder")
    public JwtEncoder refreshTokenEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(securityUtil.secretRefreshKey()));
    }

    @Bean
    @Qualifier("refreshTokenDecoder")
    public JwtDecoder refreshTokenDecoder() {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(securityUtil.secretRefreshKey()).macAlgorithm(MacAlgorithm.HS256).build();
        return refreshToken -> nimbusJwtDecoder.decode(refreshToken);
    }
}
