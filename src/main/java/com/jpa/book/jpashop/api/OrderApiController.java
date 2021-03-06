package com.jpa.book.jpashop.api;

import com.jpa.book.jpashop.domain.Address;
import com.jpa.book.jpashop.domain.Order;
import com.jpa.book.jpashop.domain.OrderItem;
import com.jpa.book.jpashop.domain.OrderStatus;
import com.jpa.book.jpashop.repository.OrderRepository;
import com.jpa.book.jpashop.repository.OrderSearch;
import com.jpa.book.jpashop.repository.order.query.OrderQueryDto;
import com.jpa.book.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> result = orderRepository.findAllByString(new OrderSearch());

        for (Order order : result) {
            order.getMember().getName(); // 멤버 프록시 초기화
            order.getDelivery().getAddress(); // 주문 배송 정보 초기화
            List<OrderItem> orderItems = order.getOrderItems(); // 주문 상품 초기

            // 주문 상품을 forEach로 순회하며 아이템 초기화
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return result;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        // ToOne 관계 먼저 모두 페치조인
        List<Order> orders = orderRepository.findAllWidthMemberDelivery(offset, limit);

        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        // ToOne 관계 먼저 모두 페치조인
        List<OrderQueryDto> orders = orderQueryRepository.findOrderQueryDtos();

        return orders;
    }



    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems().stream()
                    .map(item -> new OrderItemDto(item))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }



}
