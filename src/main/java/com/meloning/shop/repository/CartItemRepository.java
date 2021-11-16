package com.meloning.shop.repository;

import com.meloning.shop.dto.CartDetailDto;
import com.meloning.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.meloning.shop.dto.CartDetailDto(ci.id, i.name, i.price, ci.count, im.imageUrl) " +
            "from CartItem ci, ItemImage im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.representativeImage = true " +
            "order by ci.createdDate desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
