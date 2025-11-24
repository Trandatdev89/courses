package com.project01.skillineserver.entity;

import com.project01.skillineserver.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity <Long>{
    private String username;
    private String password;
    private String email;
    private String phone;
    private String fullname;
    private String avatar;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isLocked = true;
    private boolean isDisable = false;
    private boolean isAccountNonExpired = true;
    private boolean isCredentialsNonExpired = true;
}
