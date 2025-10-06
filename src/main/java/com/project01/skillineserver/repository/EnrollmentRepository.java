package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity,Long> {
}
