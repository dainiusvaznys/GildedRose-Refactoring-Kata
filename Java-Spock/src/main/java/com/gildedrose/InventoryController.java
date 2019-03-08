package com.gildedrose;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
class InventoryController {

    @GetMapping("/items")
    Collection<Item> listItems() {
        return Collections.emptyList();
    }
}
