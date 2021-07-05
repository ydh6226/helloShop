package com.ydh.helloshop.application.repository.order;

import com.ydh.helloshop.application.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, CustomOrderItemRepository {
}
