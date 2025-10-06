package com.project01.skillineserver.service.Impl;

import com.project01.skillineserver.entity.UserEntity;
import com.project01.skillineserver.enums.ErrorCode;
import com.project01.skillineserver.excepion.CustomException.AppException;
import com.project01.skillineserver.repository.UserRepository;
import com.project01.skillineserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity getMyInfo() {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        UserEntity myInfo = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOTFOUND));
        return myInfo;
    }



}
