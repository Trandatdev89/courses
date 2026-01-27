package com.project01.skillineserver.config;

import com.project01.skillineserver.utils.AuthenticationUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if(!AuthenticationUtil.isAuthenticated(authentication)){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            return Optional.of(userDetails.getUser().getId());
        }

        return Optional.empty();
    }
}
