package com.meloning.shop.repository;

import com.meloning.shop.constant.ItemDateSearchType;
import com.meloning.shop.constant.ItemSellStatus;
import com.meloning.shop.dto.ItemSearchDto;
import com.meloning.shop.entity.Item;
import com.meloning.shop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // TODO: Test Target, 추후 테스트를 통해 refactoring한 enum class 검증 필요
    private BooleanExpression createdDateAfter(String searchDateType) {
        ItemDateSearchType reqItemDateSearchType = ItemDateSearchType.fromName(searchDateType);
        // ALL 생략
        if (ItemDateSearchType.ALL.getName().equalsIgnoreCase(reqItemDateSearchType.getName()) ||
                searchDateType == null) {
            return null;
        }

        Instant dateTime = reqItemDateSearchType.compareDateTime(reqItemDateSearchType);
        return QItem.item.createdDate.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) {
            return QItem.item.name.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = jpaQueryFactory
                .selectFrom(QItem.item)
                .where(createdDateAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }
}
