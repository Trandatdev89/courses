package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.config.CustomUserDetail;
import com.project01.skillineserver.enums.Role;
import com.project01.skillineserver.utils.AuthenticationUtil;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service(value = "authorizationService")
public class AuthorizationService {

    public boolean isAdmin() {

        if (checkAuthen()) {
            return false;
        }

        CustomUserDetail customUserDetail = AuthenticationUtil.getUserDetail();
        assert customUserDetail != null;
        Role role = customUserDetail.getUser().getRole();

        return role == Role.ADMIN;
    }

    public boolean isUserNormal() {
        if (checkAuthen()) {
            return false;
        }

        CustomUserDetail customUserDetail = AuthenticationUtil.getUserDetail();
        assert customUserDetail != null;
        Role role = customUserDetail.getUser().getRole();

        return role == Role.USER;
    }

    public boolean isCanAccessApi() {
        CustomUserDetail customUserDetail = AuthenticationUtil.getUserDetail();
        return customUserDetail != null;
    }

    private boolean checkAuthen() {
        Authentication authentication = AuthenticationUtil.getAuthentication();
        if (!AuthenticationUtil.isAuthenticated(authentication)) {
            return true;
        }
        return false;
    }
}
