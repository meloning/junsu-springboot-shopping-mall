package com.meloning.shop.dto;

import com.meloning.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemName; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imageUrl; //상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imageUrl){
        this.itemName = orderItem.getItem().getName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getPrice();
        this.imageUrl = imageUrl;
    }
}
