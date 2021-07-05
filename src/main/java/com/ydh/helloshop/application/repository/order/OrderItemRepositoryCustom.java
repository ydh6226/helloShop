package com.ydh.helloshop.application.repository.order;

import com.ydh.helloshop.application.domain.order.OrderItem;
import com.ydh.helloshop.application.repository.order.dto.OrderParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemRepositoryCustom {

    Page<OrderParam> findBySearch(OrderSearch orderSearch, Pageable pageable);
}
