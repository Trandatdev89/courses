package com.project01.skillineserver.dto.request;

import com.project01.skillineserver.enums.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    private String fullname;
    private String address;
    private String email;
    private String phone;
    private Role role;
}
