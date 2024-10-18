package com.gdschongik.gdsc.domain.test.dao;

import com.gdschongik.gdsc.domain.test.domain.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long> {}
