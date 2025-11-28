package com.project01.skillineserver.service;

import com.project01.skillineserver.entity.UserEntity;

public interface UserService {
    UserEntity getMyInfo(Long id);
}
