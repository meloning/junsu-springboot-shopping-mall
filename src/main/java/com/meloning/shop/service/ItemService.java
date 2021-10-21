package com.meloning.shop.service;

import com.meloning.shop.dto.ItemFormDto;
import com.meloning.shop.dto.ItemImageDto;
import com.meloning.shop.dto.ItemSearchDto;
import com.meloning.shop.dto.MainItemDto;
import com.meloning.shop.entity.Item;
import com.meloning.shop.entity.ItemImage;
import com.meloning.shop.repository.ItemImageRepository;
import com.meloning.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
        for (int i = 0; i < itemImageFileList.size(); i++) {
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

    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {
        List<ItemImage> itemImageList = itemImageRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImageDto> itemImageDtoList = new ArrayList<>();
        for (ItemImage itemImage : itemImageList) {
            ItemImageDto itemImageDto = ItemImageDto.of(itemImage);
            itemImageDtoList.add(itemImageDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImageDtoList(itemImageDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFileList) throws Exception {
        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImageIdList = itemFormDto.getItemImageIdList();

        // 이미지 등록
        for (int i = 0; i < itemImageFileList.size(); i++) {
            itemImageService.updateItemImage(itemImageIdList.get(i), itemImageFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
