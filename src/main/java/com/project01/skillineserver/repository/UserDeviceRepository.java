package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByUserIdAndIsActive(Long userId, boolean isActive);
    Optional<UserDevice> findByDeviceId(String deviceId);
}
