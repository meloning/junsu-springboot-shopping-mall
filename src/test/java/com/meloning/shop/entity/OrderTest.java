package com.meloning.shop.entity;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.repository.ItemRepository;
import com.meloning.shop.repository.MemberRepository;
import com.meloning.shop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
class OrderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Item createItem() {
        Item newItem = new Item();
        newItem.setName("테스트 상품");
        newItem.setPrice(10000);
        newItem.setDetail("상세 설명");
        newItem.setItemSellStatus(ItemSellStatus.SELL);
        newItem.setStock(100);
        newItem.setCreatedDate(Instant.now());
        newItem.setUpdatedDate(Instant.now());
        return newItem;
    }

    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setPrice(1000);
            orderItem.setOrder(order);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        return orderRepository.save(order);
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        // given
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setPrice(1000);
            orderItem.setOrder(order);
        }

        orderRepository.saveAndFlush(order);
        entityManager.clear();

        // when
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        // given
        Order order = this.createOrder();

        // when
        order.getOrderItems().remove(0);

        // then
        entityManager.flush();
    }
}
