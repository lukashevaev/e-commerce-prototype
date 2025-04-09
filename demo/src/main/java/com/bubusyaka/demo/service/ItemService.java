package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.dto.CompositeDTO;
import com.bubusyaka.demo.model.dto.Item;
import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.repository.jpa.ItemRepository;
import com.bubusyaka.demo.repository.jpa.ProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ProviderRepository providerRepository;

    public List<Item> allItems() {
        return itemRepository.findAll()
                .stream()
                .map(entity -> new Item(entity.getId(), entity.getName(), entity.getPrice(), entity.getProviderId(), entity.getCreatedDate(), entity.getLastModifiedDate(), entity.getProductCategory(), entity.getAgeCategory()))
                .collect(Collectors.toList());
    }

    public List<Item> getItemByName(String name){
        return itemRepository.findByName(name)
                .stream()
                .map(entity -> new Item((entity.getId()), entity.getName(), entity.getPrice(), entity.getProviderId(), entity.getCreatedDate(), entity.getLastModifiedDate(), entity.getProductCategory(), entity.getAgeCategory()))
                .collect(Collectors.toList());
    }

    public ItemEntity createNewItem(Item item) {
        return providerRepository.findById(item.getProviderId())
                .map(provider -> {
                    var entity = new ItemEntity(item.getId(), item.getName(), item.getPrice(), item.getProviderId(), item.getCreatedDate(), item.getLastModifiedDate(), item.getProductCategory(), item.getAgeCategory());
                    entity.setId(item.getId());
                    return entity;

                })
                .map(entity -> itemRepository.save(entity))
                .orElseThrow(() -> new RuntimeException("Provider not found"));
    }

    public List<Item> getItemsByProviderCityInNative(List<String> city) {
        return itemRepository.findItemsByProviderCityInNative(city)
                .stream()
                .map(entity -> new Item((entity.getId()), entity.getName(), entity.getPrice(), entity.getProviderId(), entity.getCreatedDate(), entity.getLastModifiedDate(), entity.getProductCategory(), entity.getAgeCategory()))
                .collect(Collectors.toList());
    }

    public List<Item> infoAboutItems() {
        return itemRepository.findItemInformation()
                .stream()
                .map(entity -> new Item((entity.getId()), entity.getName(), entity.getPrice(), entity.getProviderId(), entity.getCreatedDate(), entity.getLastModifiedDate(), entity.getProductCategory(), entity.getAgeCategory()))
                .collect(Collectors.toList());

    }

    public String convertToCustomString(List<Item> items) {
        StringBuilder builder = new StringBuilder();
        items.stream()
                .filter(entity -> entity != null) // Фильтрация null элементов
                .forEach(item -> item.addToBuilder(builder));

        return builder.toString();
    }

    public List<Item> getItemNamesByProductCategory(List<String> productCategories) {
        return itemRepository.findItemNamesByCategory(productCategories)
                .stream()
                .map(entity -> new Item((entity.getId()), entity.getName(), entity.getPrice(), entity.getProviderId(), entity.getCreatedDate(), entity.getLastModifiedDate(), entity.getProductCategory(), entity.getAgeCategory()))
                .collect(Collectors.toList());
    }

    public String convertNameListToCustomString(List<Item> items) {
        StringBuilder builder = new StringBuilder();
        items.stream()
                .filter(entity -> entity != null) // Фильтрация null элементов
                .forEach(item -> item.addToNameBuilder(builder));

        return builder.toString();
    }

    public String convertToJson() {
        // Используем Jackson для сериализации в JSON
        //ObjectMapper mapper = new ObjectMapper();
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String jsonResponse;
        try {
            jsonResponse = mapper.writeValueAsString(infoAboutItems());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonResponse;
    }
}
