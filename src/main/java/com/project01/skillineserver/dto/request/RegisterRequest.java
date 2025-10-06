package com.project01.skillineserver.dto.request;

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
}
