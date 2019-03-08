package com.gildedrose;

import com.gildedrose.inventory.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class GildedRose {
    private static Map<String, Function<Item, InventoryItem>> inventoryTypes = new HashMap<>();

    Item[] items;

    static {
        inventoryTypes.put(AgedBrie.NAME, AgedBrie::new);
        inventoryTypes.put(Tickets.NAME, Tickets::new);
        inventoryTypes.put(LegendaryInventory.NAME, LegendaryInventory::new);
        inventoryTypes.put(ConjuredInventory.NAME, ConjuredInventory::new);
    }

    public GildedRose(Item... items) {
        // the validation belongs to Item construction, too bad we can't touch it

        String spoiled = filterInventory(i -> i.quality < 0, "is spoiled", items);
        if (!spoiled.isEmpty()) {
            throw new IllegalArgumentException(spoiled);
        }

        String expired = filterInventory(i -> i.sellIn < 0, "is past expiration", items);
        if (!expired.isEmpty()) {
            throw new IllegalArgumentException(expired);
        }

        this.items = items;
    }

    Item[] updateQuality() {
        Stream.of(items)
                .parallel()
                .map(GildedRose::classifyInventory)
                .forEach(InventoryItem::rollDay);
        return items;
    }

    private static InventoryItem classifyInventory(Item item) {
        return inventoryTypes.getOrDefault(item.name, InventoryItem::new).apply(item);
    }

    private static String filterInventory(Predicate<Item> constraint, String description, Item... items) {
        return Stream.of(items)
                .filter(constraint)
                .map(i -> i.name + " " + description)
                .collect(joining(", "));
    }
}
