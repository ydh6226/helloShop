package com.ydh.helloshop.application.service;

import com.ydh.helloshop.application.controller.order.dto.CreateOrderParam;
import com.ydh.helloshop.application.controller.order.dto.RequestOrderInfo;
import com.ydh.helloshop.application.domain.item.Album;
import com.ydh.helloshop.application.domain.item.Item;
import com.ydh.helloshop.application.domain.item.ItemCategory;
import com.ydh.helloshop.application.domain.item.ItemParam;
import com.ydh.helloshop.application.domain.member.CreateMemberParam;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.domain.order.Order;
import com.ydh.helloshop.application.domain.order.OrderStatus;
import com.ydh.helloshop.application.exception.NotEnoughStockException;
import com.ydh.helloshop.application.repository.MemberRepository;
import com.ydh.helloshop.application.repository.item.ItemRepository;
import com.ydh.helloshop.application.repository.order.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ItemRepository itemRepository;

    private static final int ITEM1_PRICE = 10000;
    private static final int ITEM2_PRICE = 20000;
    private static final int ITEM1_STOCK_QUANTITIY = 100;
    private static final int ITEM2_STOCK_QUANTITIY = 100;
    private static final int ITEM1_ORDER_COUNT = 10;
    private static final int ITEM2_ORDER_COUNT = 30;

    @Test
    @DisplayName("주문 생성")
    void createOrder() throws Exception {
        //given
        Item item1 = createItem(ITEM1_PRICE, ITEM1_STOCK_QUANTITIY);
        Item item2 = createItem(ITEM2_PRICE, ITEM2_STOCK_QUANTITIY);
        Member member = createMember();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(itemRepository.findById(anyLong()))
                .willReturn(Optional.of(item1))
                .willReturn(Optional.of(item2));
        given(orderRepository.save(any())).will(AdditionalAnswers.returnsFirstArg());

        //when
        Order order = orderService.createOrder(createOrderParam(ITEM1_ORDER_COUNT, ITEM2_ORDER_COUNT), 0L);

        //then
        assertThat(item1.getStockQuantity()).isEqualTo(ITEM1_STOCK_QUANTITIY - ITEM1_ORDER_COUNT);
        assertThat(item2.getStockQuantity()).isEqualTo(ITEM2_STOCK_QUANTITIY - ITEM2_ORDER_COUNT);
        assertThat(order.getTotalPrice()).isEqualTo(ITEM1_PRICE * ITEM1_ORDER_COUNT + ITEM2_PRICE * ITEM2_ORDER_COUNT);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("주문 생성 실패 - 재고수량부족")
    void createOrderWithInsufficientStockQuantity() throws Exception {
        //given
        Item item = createItem(ITEM1_PRICE, 0);
        Member member = createMember();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(itemRepository.findById(anyLong()))
                .willReturn(Optional.of(item));

        //when
        Assertions.assertThrows(NotEnoughStockException.class,
                () -> orderService.createOrder(createOrderParam(ITEM1_ORDER_COUNT), 0L));

        //then
        assertThat(item.getStockQuantity()).isEqualTo(0);
    }
    


    private Member createMember() {
        return Member.createMember(new CreateMemberParam());
    }

    private Item createItem(int price, int stockQuantity) {
        ItemParam itemParam = new ItemParam();
        itemParam.setPrice(price);
        itemParam.setStockQuantity(stockQuantity);
        itemParam.setItemCategory(ItemCategory.createItemCategory(null));

        Album item = new Album("artist", "etc");
        item.setBasicInfo(itemParam);

        return item;
    }

    private CreateOrderParam createOrderParam(int... counts) {
        CreateOrderParam orderParam = new CreateOrderParam();
        for (int count : counts) {
            orderParam.getRequestOrderInfos()
                    .add(createRequestOrderInfo(count));
        }
        return orderParam;
    }

    private RequestOrderInfo createRequestOrderInfo(int count) {
        RequestOrderInfo requestOrderInfo = new RequestOrderInfo();
        requestOrderInfo.setItemId(1L);
        requestOrderInfo.setCount(count);
        return requestOrderInfo;
    }
}