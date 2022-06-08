package com.jpa.book.jpashop.service;

import com.jpa.book.jpashop.domain.Address;
import com.jpa.book.jpashop.domain.Item.Book;
import com.jpa.book.jpashop.domain.Item.Item;
import com.jpa.book.jpashop.domain.Item.NotEnoughStockException;
import com.jpa.book.jpashop.domain.Member;
import com.jpa.book.jpashop.domain.Order;
import com.jpa.book.jpashop.domain.OrderStatus;
import com.jpa.book.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember("회원1", new Address("서울", "강가", "123-123"));
        Item book = createBook("JPA!", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);


        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, order.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, order.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember("회원3", new Address("서울", "화양동", "123-456"));
        Item item = createBook("JPA3", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // when
        orderService.cancelOrder(orderId);

        // then
        Order canceledOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, canceledOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, item.getStockQuantity());
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember("회원2", new Address("서울", "강가", "123-123"));
        Item book = createBook("JPA2", 10000, 10);

        int orderCount = 11;

        // when, then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount), "재고 수량 부족 예외가 발생해야 한다.");
    }

    private Item createBook(String name, int price, int quantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String name, Address address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        em.persist(member);
        return member;
    }
}