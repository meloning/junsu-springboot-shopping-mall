package com.meloning.shop.controller;

import com.meloning.shop.dto.ItemFormDto;
import com.meloning.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDetail";
    }
}
