package com.meloning.shop.service;

import com.meloning.shop.dto.ItemFormDto;
import com.meloning.shop.entity.Item;
import com.meloning.shop.entity.ItemImage;
import com.meloning.shop.repository.ItemImageRepository;
import com.meloning.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;
    private final ItemImageRepository itemImageRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFileList) throws Exception {
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 이미지 등록
        for (int i=0; i<itemImageFileList.size(); i++) {
            ItemImage itemImage = new ItemImage();
            itemImage.setItem(item);
            if (i == 0) {
               itemImage.setRepresentativeImage(true);
            } else {
                itemImage.setRepresentativeImage(false);
            }
            itemImageService.saveItemImage(itemImage, itemImageFileList.get(i));
        }

        return item.getId();
    }
}
