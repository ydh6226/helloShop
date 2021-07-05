package com.ydh.helloshop.application.repository.order;

import com.ydh.helloshop.application.domain.order.OrderItem;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomOrderItemRepository {

    PageImpl<OrderItem> findBySearch(OrderSearch orderSearch, Pageable pageable);
}
