package com.ydh.helloshop.service;

import com.ydh.helloshop.domain.Address;
import com.ydh.helloshop.domain.Member;
import com.ydh.helloshop.domain.Order;
import com.ydh.helloshop.domain.OrderStatus;
import com.ydh.helloshop.exception.NoSuchItem;
import com.ydh.helloshop.exception.NotEnoughStockException;
import com.ydh.helloshop.item.Album;
import com.ydh.helloshop.item.Book;
import com.ydh.helloshop.item.Item;
import com.ydh.helloshop.repository.item.ItemRepository;
import com.ydh.helloshop.repository.MemberRepository;
import com.ydh.helloshop.repository.OrderRepository;
import com.ydh.helloshop.repository.OrderSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.ydh.helloshop.domain.MemberStatus.CUSTOMER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("상품주문")
    public void order() throws Exception {
        //given
        List<Long> ids = dbInit();

        //when
        int orderCount = 3;
        Long orderId = orderService.orderOne(ids.get(0), ids.get(2), orderCount);

        Order findOrder = orderRepository.findOne(orderId);
        Item findItem = itemRepository.findById(ids.get(2)).orElseThrow(() -> new NoSuchItem("Register the Item!!"));

        //then
        //org.junit.Assert.assertEquals
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문 가격은 가격 * 수량 이다.", 3 * 10000, findOrder.getTotalPrice());
        assertEquals("주문한 상품 종류수가 정확해야 한다.", 1, findOrder.getOrderItems().size());
        assertEquals("주문 수량만큼 재고가 감소해야 한다.", 997, findItem.getStockQuantity());
    }
    
    @Test
    @DisplayName("단일주문 상품조회")
    public void searchSingleOrder() throws Exception {
        //given
        List<Long> ids = dbInit();

        List<Long> itemIds = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        itemIds.add(ids.get(3));
        itemIds.add(ids.get(4));

        counts.add(5);
        counts.add(10);

        //when
        Long orderId = orderService.orderMultiple(ids.get(0), itemIds, counts);

        Order order = orderRepository.findOne(orderId);
        Item item1 = itemRepository.findById(ids.get(3)).orElseThrow(() -> new NoSuchItem("Register the Item!!"));
        Item item2 = itemRepository.findById(ids.get(4)).orElseThrow(() -> new NoSuchItem("Register the Item!!"));

        //then
        assertEquals("주문한 상품 종류 수는 정확해야 한다.", 2, order.getOrderItems().size());
        assertEquals("주문 수량만큼 재고가 감소해야 한다.", 995, item1.getStockQuantity());
        assertEquals("주문 수량만큼 재고가 감소해야 한다.", 990, item2.getStockQuantity());
        assertEquals("주문 가격은 정확해야 한다.", 5 * 20000 + 10 * 5000, order.getTotalPrice());
    }

    @Test
    @DisplayName("복수주문 상품조회")
    public void searchMultipleOrder() throws Exception {
        //given
        List<Long> ids = dbInit();
        Member member1 = memberRepository.findOne(ids.get(0));

        List<Long> itemIds = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        itemIds.add(ids.get(3));
        itemIds.add(ids.get(4));

        counts.add(5);
        counts.add(10);

        //when
        orderService.orderMultiple(ids.get(0), itemIds, counts);
        orderService.orderMultiple(ids.get(1), itemIds, counts);

        int size1 = orderRepository.findAll(new OrderSearch()).size();
        int size2 = orderRepository.findAll(new OrderSearch(member1.getId(), null, null)).size();
        int size3 = orderRepository.findAll(new OrderSearch(null, null, OrderStatus.ORDER)).size();
        int size4 = orderRepository.findAll(new OrderSearch(null, "ON AIR", null)).size();
        int size5 = orderRepository.findAll(new OrderSearch(member1.getId(), "ON AIR", null)).size();

        //then
        assertEquals("전체 주문 개수는 정확해야 한다.", 2, size1);
        assertEquals("특정 회원의 주문 개수는 정확해야 한다.", 1, size2);
        assertEquals("특정 상태의 주문 개수는 정확해야 한다.", 2, size3);
        assertEquals("특정 상품 주문 개수는 정확해야 한다.", 2, size4);
        assertEquals("특정 회원이 주문한 특정 상품 주문 개수는 정확해야 한다.", 1, size5);
    }
    
    @Test
    @DisplayName("상품주문 재고수량초과")
    public void overStockOrder() throws Exception {
        //given
        List<Long> ids = dbInit();

        //when
        int orderCount = 2000;

        //then
        NotEnoughStockException exception = assertThrows(NotEnoughStockException.class,
                        () -> orderService.orderOne(ids.get(0), ids.get(2), orderCount));
        assertEquals("need more stock!!", exception.getMessage());
    }

    @Test
    @DisplayName("주문 취소")
    public void cancelOrder() throws Exception {
        //given
        List<Long> ids = dbInit();

        //when
        int orderCount = 10;
        Long orderId = orderService.orderOne(ids.get(0), ids.get(2), 10);
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);
        Item findItem = itemRepository.findById(ids.get(2)).orElseThrow(() -> new NoSuchItem("Register the Item!!"));

        assertEquals("취소된 주문의 상태는 CANCEL이다", OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문 취소된 상품의 재고수량이 원복되어야 한다.", 1000, findItem.getStockQuantity());
    }

    
    /**
     * @return {회원1 id, 회원2 id, 상품1 id, 상품2 id, 상품3 id}
     */
    public List<Long> dbInit() {
        //회원
        Member member1 = new Member();
        member1.createInfo("userA", "userA@gmail.com", "1234",
                new Address("서울", "행신로", "1234"), CUSTOMER);

        Member member2 = new Member();
        member2.createInfo("userB", "userB@gmail.com", "2222",
                new Address("경기", "강남로", "4567"), CUSTOMER);

        em.persist(member1);
        em.persist(member2);

        //상품
        Book book1 = new Book("김영한", "1234", "jpa프로그래밍", 10000, 1000);
        Book book2 = new Book("홍길동", "3333", "어서와 스프링", 20000, 1000);
        Album album1 = new Album("릴보이", "good", "ON AIR", 5000, 1000);

        em.persist(book1);
        em.persist(book2);
        em.persist(album1);

        List<Long> ids = new ArrayList<>();
        ids.add(member1.getId());
        ids.add(member2.getId());
        ids.add(book1.getId());
        ids.add(book2.getId());
        ids.add(album1.getId());

        return ids;
    }
}