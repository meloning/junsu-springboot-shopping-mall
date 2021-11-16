package com.meloning.shop.constant;

import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Getter
public enum ItemDateSearchType {
    ALL("all", 0, null),
    ONE_DAY("1d", 1,  ChronoUnit.DAYS),
    ONE_WEEK("1w", 1, ChronoUnit.WEEKS),
    ONE_MONTH("1m", 1, ChronoUnit.MONTHS),
    SIX_MONTH("6m", 6, ChronoUnit.MONTHS);

    private final String name;
    private final Integer number;
    private final TemporalUnit temporalUnit;

    private static final Map<String, ItemDateSearchType> ITEM_DATE_SEARCH_TYPE_MAP = Stream.of(values()).collect(toMap(e -> e.name, e -> e));

    ItemDateSearchType(String name, Integer number, TemporalUnit temporalUnit) {
        this.name = name;
        this.number = number;
        this.temporalUnit = temporalUnit;
    }

    public static ItemDateSearchType fromName(String name) {
        return (name == null) ? ItemDateSearchType.ALL : ITEM_DATE_SEARCH_TYPE_MAP.get(name);
    }

    public Instant compareDateTime(ItemDateSearchType reqItemDateSearchType) {
        Instant dateTime = Instant.now();
        switch (reqItemDateSearchType) {
            case ONE_DAY:
            case ONE_WEEK:
            case ONE_MONTH:
            case SIX_MONTH:
                return dateTime.minus(number, temporalUnit);
            default:
                throw new RuntimeException("존재하지 않는 ItemDateSearchType입니다.");
        }
    }
}
