package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
}
