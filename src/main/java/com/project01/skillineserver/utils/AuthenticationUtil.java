package com.project01.skillineserver.utils;

import com.project01.skillineserver.config.CustomUserDetail;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class AuthenticationUtil {

    public static boolean isAuthenticated(Authentication authentication){

        if(authentication==null){
            return false;
        }

        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static CustomUserDetail getUserDetail(){
        Authentication authentication = getAuthentication();
        if(isAuthenticated(authentication)){
            return (CustomUserDetail)authentication.getPrincipal();
        }

        return null;
    }

}
