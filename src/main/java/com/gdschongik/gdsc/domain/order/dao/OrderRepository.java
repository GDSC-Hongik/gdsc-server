package com.gdschongik.gdsc.domain.order.dao;

import com.gdschongik.gdsc.domain.order.domain.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {
    Optional<Order> findByNanoId(String nanoId);
}
