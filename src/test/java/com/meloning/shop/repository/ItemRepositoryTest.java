package com.meloning.shop.repository;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        // given
        Item item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setCreatedDate(Instant.now());
        item.setUpdatedDate(Instant.now());

        // when
        Item savedItem = itemRepository.save(item);

        // then
        assertThat(item.getName()).isEqualTo(savedItem.getName());
    }

    public void createItemList() {
        for (int i=1; i<=10; i++) {
            Item item = new Item();
            item.setName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStock(100);
            item.setCreatedDate(Instant.now());
            item.setUpdatedDate(Instant.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByNameTest() {
        // given
        this.createItemList();
        final String testItemName = "테스트 상품1";

        // when
        List<Item> itemList = itemRepository.findByName(testItemName);

        // then
        assertThat(itemList).extracting("name").contains(testItemName);
    }

    @Test
    @DisplayName("상품명, 상품 상세 설명 or 테스트")
    public void findByNameOrDetailTest() {
        // given
        this.createItemList();
        final String testItemName1 = "테스트 상품1";
        final String testItemDetail1 = "테스트 상품 상세 설명1";
        final String testItemName5 = "테스트 상품5";
        final String testItemDetail5 = "테스트 상품 상세 설명5";

        // when
        List<Item> itemList = itemRepository.findByNameOrDetail(testItemName1, testItemDetail5);

        // then
        assertThat(itemList.size()).isEqualTo(2);
        assertThat(itemList).extracting("name", "detail")
                .contains(
                        tuple(testItemName1, testItemDetail1),
                        tuple(testItemName5, testItemDetail5)
                );
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        // given
        this.createItemList();
        final int testPrice = 10005;

        // when
        List<Item> itemList = itemRepository.findByPriceLessThan(testPrice);

        // then
        assertThat(itemList.size()).isEqualTo(4);
        assertThat(itemList).extracting("price")
                .contains(10001, 10002, 10003, 10004);
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDescTest() {
        // given
        this.createItemList();
        final int testPrice = 10005;

        // when
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(testPrice);

        // then
        assertThat(itemList.size()).isEqualTo(4);
        assertThat(itemList).extracting("price")
                .containsExactly(10004, 10003, 10002, 10001);
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByDetailTest() {
        // given
        this.createItemList();

        // when
        List<Item> itemList = itemRepository.findByDetail("테스트 상품 상세 설명");

        // then
        assertThat(itemList.size()).isEqualTo(10);
        assertThat(itemList).extracting("price")
                .containsExactly(10010, 10009, 10008, 10007, 10006, 10005, 10004, 10003, 10002, 10001);
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스ㅌ")
    public void findByDetailByNativeTest() {
        // given
        this.createItemList();

        // when
        List<Item> itemList = itemRepository.findByDetailByNative("테스트 상품 상세 설명");

        // then
        assertThat(itemList.size()).isEqualTo(10);
        assertThat(itemList).extracting("price")
                .containsExactly(10010, 10009, 10008, 10007, 10006, 10005, 10004, 10003, 10002, 10001);
    }
}