package com.meloning.shop.repository;

import com.meloning.shop.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemIdOrderByIdAsc(Long itemId);
}
