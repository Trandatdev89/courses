package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.OrderEntity;
import com.project01.skillineserver.projection.OrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT od.id AS id, " +
            "od.status AS status, " +
            "od.createAt AS createdAt, " +
            "od.quantity AS quantity, " +
            "od.totalPrice AS totalPrice, " +
            "us.username AS username, " +
            "us.address AS address, " +
            "us.fullname AS fullname, " +
            "us.email AS email, " +
            "us.phone AS phone " +
            "FROM OrderEntity od " +
            "INNER JOIN UserEntity us ON us.id = od.userId " +
            "WHERE :keyword IS NULL OR LOWER(us.username) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<OrderProjection> getOrders(String keyword, Pageable pageable);
}
