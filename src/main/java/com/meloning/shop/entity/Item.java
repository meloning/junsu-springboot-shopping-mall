package com.meloning.shop.entity;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.dto.ItemFormDto;
import com.meloning.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Lob
    @Column(nullable = false)
    private String detail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
        this.detail = itemFormDto.getDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stock - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException(String.format("상품의 재고가 부족 합니다. (현재 재고 수량: %s)", this.stock));
        }

        this.stock = restStock;
    }

    public void addStock(int stockNumber){
        this.stock += stockNumber;
    }
}
