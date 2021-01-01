package com.ydh.helloshop.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;

@Entity
@Getter
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
}
