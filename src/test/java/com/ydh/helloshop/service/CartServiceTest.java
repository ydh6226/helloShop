package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Cart;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.CartItemRepository;
import com.ydh.helloshop.service.item.ItemServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired CartService cartService;

    @Autowired ItemServiceImpl itemService;

    @Autowired CartItemRepository cartItemRepository;

    @Autowired EntityManager em;

    @Test
    @DisplayName("장바구니에 상품 추가")
    @Rollback(value = false)
    public void addToCart() throws Exception {
        //given
        Item item = itemService.findOne(1L);

        //when
        Long cartId = cartService.addToCart(2L, item, 2);

        em.flush();
        em.clear();

        //then
        Cart cart = cartService.findOne(cartId);
        assertThat(cart.getCartItems().size()).isEqualTo(1);
        assertThat(cart.getTotalPrice()).isEqualTo(20000);
    }

    @Test
    @DisplayName("장바구니에서 상품 삭제")
    @Rollback(value = false)
    public void deleteFormCart() throws Exception {
        //given
        Item item1 = itemService.findOne(1L);
        Item item2 = itemService.findOne(2L);

        cartService.addToCart(2L, item1, 2);
        cartService.addToCart(2L, item2, 2);

        em.flush();
        em.clear();

        //when
        Long cartId = cartService.deleteItemsFrommCart(2L, Arrays.asList(1L, 2L));

        em.flush();
        em.clear();

        //then
        Cart cart = cartService.findOne(cartId);
        assertThat(cart.getCartItems().size()).isEqualTo(0);
        assertThat(cart.getTotalPrice()).isEqualTo(0);
    }
}