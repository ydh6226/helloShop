package com.ydh.helloshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.ydh.helloshop.domain.DeliveryStatus.READY;
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
    Order order;

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
    public void initOrder(Order order) {
        this.order = order;
    }


}
