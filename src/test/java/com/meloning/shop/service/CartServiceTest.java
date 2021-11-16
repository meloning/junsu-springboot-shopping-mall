package com.meloning.shop.service;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.dto.CartItemDto;
import com.meloning.shop.entity.CartItem;
import com.meloning.shop.entity.Item;
import com.meloning.shop.entity.Member;
import com.meloning.shop.repository.CartItemRepository;
import com.meloning.shop.repository.ItemRepository;
import com.meloning.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setEmail("melon8372@gmail.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void testAddCart() {
        // given
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        // when
        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        // then
        assertThat(item.getId()).isEqualTo(cartItem.getItem().getId());
        assertThat(cartItemDto.getCount()).isEqualTo(cartItem.getCount());
    }
}