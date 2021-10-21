package com.meloning.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String name;
    private String detail;
    private String imageUrl;
    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, String name, String detail, String imageUrl, Integer price) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.imageUrl = imageUrl;
        this.price = price;
    }
}
