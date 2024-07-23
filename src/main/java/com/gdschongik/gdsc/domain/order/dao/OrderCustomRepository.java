package com.gdschongik.gdsc.domain.order.dao;

import com.gdschongik.gdsc.domain.order.dto.request.OrderQueryOption;
import com.gdschongik.gdsc.domain.order.dto.response.OrderAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {
    Page<OrderAdminResponse> searchOrders(OrderQueryOption queryOption, Pageable pageable);
}
