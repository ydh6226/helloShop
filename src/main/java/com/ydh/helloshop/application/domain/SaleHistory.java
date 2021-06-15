package com.ydh.helloshop.application.domain;

import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class SaleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_history_id_gen")
    @SequenceGenerator(name = "sale_history_id_gen", sequenceName = "sale_history_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    private LocalDateTime saleDate;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    private int totalPrice;

}
