package com.ydh.helloshop.application.domain.delivery;

import com.ydh.helloshop.application.domain.Address;
import com.ydh.helloshop.application.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.ydh.helloshop.application.domain.delivery.DeliveryStatus.READY;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(generator = "delivery_id")
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private OrderItem orderItem;

    @Embedded
    private Address address;

    @Enumerated(STRING)
    private DeliveryStatus status;

    //생성자
    public Delivery(Address address) {
        this.address = address;
        this.status = READY;
    }

    //setter
    public void changeOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    //== 비즈니스 로직 ==//
    public void startDelivery() {
        status = DeliveryStatus.SHIPPED;
    }

    public void endDelivery() {
        status = DeliveryStatus.COMP;
    }
}
