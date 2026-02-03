package com.project01.skillineserver.config;

import com.project01.skillineserver.constants.AppConstants;
import com.project01.skillineserver.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {

    @Override
    public String resolve(HttpServletRequest request) {
        return CookieUtil.getTokenFromCookie(AppConstants.ACCESS_TOKEN,request);
    }
}
