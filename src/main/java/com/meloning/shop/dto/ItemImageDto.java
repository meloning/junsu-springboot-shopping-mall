package com.meloning.shop.dto;

import com.meloning.shop.entity.ItemImage;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImageDto {
    private Long id;
    private String imageName;
    private String originalImageName;
    private String imageUrl;
    private Boolean representativeImage;

    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemImageDto of(ItemImage itemImage) {
        return modelMapper.map(itemImage, ItemImageDto.class);
    }
}
