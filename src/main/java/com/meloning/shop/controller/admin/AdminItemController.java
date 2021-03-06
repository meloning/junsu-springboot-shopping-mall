package com.meloning.shop.controller.admin;

import com.meloning.shop.dto.ItemFormDto;
import com.meloning.shop.dto.ItemSearchDto;
import com.meloning.shop.entity.Item;
import com.meloning.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminItemController {
    private final ItemService itemService;

    @GetMapping(value = {"/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);
        return "item/itemManage";
    }

    @GetMapping("/item/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping("/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("itemImageFile") List<MultipartFile> itemImageFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImageFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "????????? ?????? ???????????? ?????? ?????? ????????????.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImageFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "?????? ?????? ??? ????????? ?????????????????????.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/item/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "???????????? ?????? ???????????????.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/item")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
                             @RequestParam("itemImageFile") List<MultipartFile> itemImageFileList,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (itemImageFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "????????? ?????? ???????????? ?????? ?????? ????????????.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImageFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "?????? ?????? ??? ????????? ?????????????????????.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
}
