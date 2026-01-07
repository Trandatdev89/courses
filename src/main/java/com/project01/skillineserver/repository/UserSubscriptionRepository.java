package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
    List<UserSubscription> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    Optional<UserSubscription> findByUserIdAndEndpoint(Long userId, String endpoint);
}
