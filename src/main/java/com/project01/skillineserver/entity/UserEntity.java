package com.project01.skillineserver.entity;

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
    private boolean authenticate;
    private Long role_id;
}
