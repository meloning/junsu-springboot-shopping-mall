package com.meloning.shop.service;

import com.meloning.shop.entity.ItemImage;
import com.meloning.shop.repository.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.io.File;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemImageService {
    @Value("${upload.item-image}")
    private String itemImageLocation;

    private final ItemImageRepository itemImageRepository;
    private final FileService fileService;

    public void saveItemImage(ItemImage itemImage, MultipartFile itemImageFile) throws Exception {
        String originalImageName = itemImageFile.getOriginalFilename();
        String imageName = "";
        String imageUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(originalImageName)) {
            imageName = fileService.uploadFile(itemImageLocation, originalImageName, itemImageFile.getBytes());
            imageUrl = "/images/item/" + imageName;
        }

        // 상품 이미지 정보 저장
        itemImage.updateItemImage(originalImageName, imageName, imageUrl);
        itemImageRepository.save(itemImage);
    }

    public void updateItemImage(Long itemImageId, MultipartFile itemImageFile) throws Exception {
        if(!itemImageFile.isEmpty()) {
            ItemImage savedItemImage = itemImageRepository.findById(itemImageId)
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImage.getImageName())) {
                fileService.deleteFile(itemImageLocation + File.separator + savedItemImage.getImageName());
            }

            String originalImageName = itemImageFile.getOriginalFilename();
            String imageName = fileService.uploadFile(itemImageLocation, originalImageName, itemImageFile.getBytes());
            String imageUrl = "/images/item" + imageName;
            savedItemImage.updateItemImage(originalImageName, imageName, imageUrl);
        }
    }
}
