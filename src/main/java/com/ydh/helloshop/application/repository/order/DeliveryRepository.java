package com.ydh.helloshop.application.repository.order;

import com.ydh.helloshop.application.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}
