package com.meloning.shop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
public class ItemDto {
    private Long id;
    private String name;
    private int price;
    private String detail;
    private String sellStatCd;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
