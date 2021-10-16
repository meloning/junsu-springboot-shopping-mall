package com.meloning.shop.repository;

import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.entity.Item;
import com.meloning.shop.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager entityManager;

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

        // when
        Item savedItem = itemRepository.save(item);

        // then
        assertThat(item.getName()).isEqualTo(savedItem.getName());
    }

    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStock(100);
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

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        // given
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.detail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        // when
        List<Item> itemList = query.fetch();

        // then
        assertThat(itemList.size()).isEqualTo(10);
        assertThat(itemList).extracting("price")
                .containsExactly(10010, 10009, 10008, 10007, 10006, 10005, 10004, 10003, 10002, 10001);
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStock(100);
            Item savedItem = itemRepository.save(item);
        }
        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStock(100);
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2() {
        // given
        this.createItemList2();

        final String testDetail = "테스트 상품 상세 설명";
        final int testPrice = 10003;
        final String testItemSellState = "SELL";

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        booleanBuilder.and(item.detail.like("%" + testDetail + "%"));
        booleanBuilder.and(item.price.gt(testPrice));

        if(StringUtils.equals(testItemSellState, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0, 5);

        // when
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println(String.format("Total Element : %s", itemPagingResult.getTotalElements()));

        // then
        List<Item> resultItemList = itemPagingResult.getContent();
        assertThat(resultItemList.size()).isEqualTo(2);
        assertThat(resultItemList).extracting("price", "itemSellStatus")
                .containsExactly(
                        tuple(10004, ItemSellStatus.SELL),
                        tuple(10005, ItemSellStatus.SELL)
                );
    }
}
