package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.entity.ItemEntityTest;
import org.springframework.stereotype.Service;

@Service("ITEM_TEST_FORMATTER")
public class ItemTestEntityFormatter implements EntityFormatter<ItemEntityTest> {

    @Override
    public String convert(ItemEntityTest entity) {
        return entity.toString();
    }

}
