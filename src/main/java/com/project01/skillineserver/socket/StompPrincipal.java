package com.project01.skillineserver.socket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Principal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StompPrincipal implements Principal {

    private String username;

    @Override
    public String getName() {
        return this.username;
    }
}
