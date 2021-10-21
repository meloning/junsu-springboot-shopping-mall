package com.meloning.shop.dto;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotNull(message = "이름은 필수 입력 값입니다.")
    private String detail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stock;

    private ItemSellStatus itemSellStatus;
    private List<ItemImageDto> itemImageDtoList = new ArrayList<>();
    private List<Long> itemImageIdList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}
