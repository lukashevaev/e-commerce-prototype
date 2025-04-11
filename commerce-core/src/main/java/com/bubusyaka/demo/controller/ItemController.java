package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.Item;
import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.repository.jpa.ItemRepository;
import com.bubusyaka.demo.repository.jpa.ItemTestRepository;
import com.bubusyaka.demo.service.ItemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/items")
@Tag(name = "Item controller", description = "Shows information about items")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ItemTestRepository itemTestRepository;

    @GetMapping
    public List<Item> items() {
        List<Item> items = new ArrayList<>();

        return items;
    }

    @Autowired
    public ItemController(ItemService itemService, ItemRepository itemRepository, ItemTestRepository itemTestRepository) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.itemTestRepository = itemTestRepository;
    }

    @Operation(summary = "Shows creation date of an item")
    @GetMapping("/audit")
    public LocalDateTime audit(@Parameter(description = "id of item to be searched")
                                   Long id) {
        var itemEntity = itemTestRepository.findById(id);
        return itemEntity.get().getCreatedDate();
    }

    @Operation(summary = "Shows all items")
    @GetMapping("/all")
    public List<Item> allItems() { // TODO change repo to service
        /*return itemRepository.findAll()
                .stream()
                .map(entity -> new Item(entity.getId(),entity.getName(),entity.getPrice()))
                .collect(Collectors.toList());*/

        return itemService.allItems();
    }

    @Operation(summary = "Shows items by name parameter")
    @GetMapping("/name")
    public List<Item> getItemByName(@Parameter(description = "name of item to be searched")
            @RequestParam String name) {
        return itemService.getItemByName(name);
    }

    @Operation(summary = "Creates new item")
    @PostMapping
    public ItemEntity createNewItem(@RequestBody Item item) throws JsonProcessingException {
        return itemService.createNewItem(item);
    }

    @Operation(summary = "Shows items by provider city parameter")
    @GetMapping("/cities")
    //@RequestMapping(value = "/cities", method = RequestMethod.GET,consumes = "application/json")
    public List<Item> getItemsByProviderCityInNative(@Parameter(description = "provider city of item to be searched")
                                                         @RequestBody List<String> cities2) {
        return itemService.getItemsByProviderCityInNative(cities2);
    }
}
