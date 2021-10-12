package com.meloning.shop.controller;

import com.meloning.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafExController {

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setDetail("상품 상세 설명");
        itemDto.setName("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setCreatedAt(ZonedDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafExample03(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++ ) {
            ItemDto itemDto = new ItemDto();
            itemDto.setDetail("상품 상세 설명" + i);
            itemDto.setName("테스트 상품" + i);
            itemDto.setPrice(1000*i);
            itemDto.setCreatedAt(ZonedDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    @GetMapping("/ex04")
    public String thymeleafExample04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++ ) {
            ItemDto itemDto = new ItemDto();
            itemDto.setDetail("상품 상세 설명" + i);
            itemDto.setName("테스트 상품" + i);
            itemDto.setPrice(1000*i);
            itemDto.setCreatedAt(ZonedDateTime.now());

            itemDtoList.add(itemDto);
        }

        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping("/ex05")
    public String thymeleafExample05(Model model) {
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafExample06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping("/ex07")
    public String thymeleafExample07(Model model) {
        return "thymeleafEx/thymeleafEx07";
    }
}
