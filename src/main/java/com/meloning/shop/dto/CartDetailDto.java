package com.meloning.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartDetailDto {
    private Long cartItemId; // 장바구니 상품 아이디
    private String itemName; // 상품명
    private int price; // 상품 금액
    private int count; // 수량
    private String imageUrl; // 상품 이미지 경로

    public CartDetailDto(Long cartItemId, String itemName, int price, int count, String imageUrl) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imageUrl = imageUrl;
    }
}
