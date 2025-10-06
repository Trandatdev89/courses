package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Long>{

    Page<OrderEntity> findAll(Pageable pageable);
}
