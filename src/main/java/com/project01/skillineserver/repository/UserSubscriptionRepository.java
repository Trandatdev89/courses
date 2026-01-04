package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
    List<UserSubscription> findByUserId(Long userId);
}
