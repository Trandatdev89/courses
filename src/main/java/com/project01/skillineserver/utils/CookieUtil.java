package com.project01.skillineserver.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    public static void setAccessTokenCookieHttpOnly(String accessToken, HttpServletResponse response){
        Cookie accessTokenCookie  = new Cookie("accessToken",accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(15 * 60);
        accessTokenCookie.setAttribute("SameSite", "Strict");
        response.addCookie(accessTokenCookie);
    }

    public static void setRefreshTokenCookieHttpOnly(String refreshTokenCookieHttpOnly, HttpServletResponse response){
        Cookie refreshTokenCookie  = new Cookie("refreshToken",refreshTokenCookieHttpOnly);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        response.addCookie(refreshTokenCookie);
    }

    public static void setDeviceIdCookieHttpOnly(String currentDeviceId, HttpServletResponse response){
        Cookie deviceIdCookie = new Cookie("deviceId", currentDeviceId);
        deviceIdCookie.setHttpOnly(false); // Frontend cần đọc được
        deviceIdCookie.setSecure(false);
        deviceIdCookie.setPath("/");
        deviceIdCookie.setMaxAge(30 * 24 * 60 * 60);
        deviceIdCookie.setAttribute("SameSite", "Strict");
        response.addCookie(deviceIdCookie);
    }

    public static String getTokenFromCookie(String name, HttpServletRequest request){
        if(request.getCookies()==null){
            return null;
        }
        for (Cookie c : request.getCookies()){
            if(name.equals(c.getName())){
                return c.getValue();
            }
        }
        return null;
    }
}
