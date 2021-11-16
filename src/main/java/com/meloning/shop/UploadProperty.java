package com.meloning.shop;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Getter
@Configuration
public class UploadProperty {
    @Value("${upload.item-image}")
    private String itemImageLocation;

    @PostConstruct
    public void init() {
        log.info(itemImageLocation);
    }
}
