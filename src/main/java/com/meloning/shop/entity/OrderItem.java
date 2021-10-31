package com.meloning.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int price;

    private int count;

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }

    public void setOrder(Order order) {
        this.order = order;

        // 무한루프에 빠지지 않도록 체크
        if (!order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }

    public int getTotalPrice() {
        return price * count;
    }
}
