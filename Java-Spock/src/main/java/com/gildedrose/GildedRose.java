package com.gildedrose;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class GildedRose {
    Item[] items;

    GildedRose(Item... items) {
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

    private InventoryItem classifyInventory(Item item) {
        if (item.name.equals(AgedBrie.NAME)) {
            return new AgedBrie(item);
        }
        if (item.name.equals(TicketInventory.NAME)) {
            return new TicketInventory(item);
        }
        if (item.name.equals(LegendaryInventory.NAME)) {
            return new LegendaryInventory(item);
        }

        return new InventoryItem(item);
    }

    Item[] updateQuality() {
        Stream.of(items)
                .map(this::classifyInventory)
                .forEach(InventoryItem::rollDay);

        return items;
    }

    private static String filterInventory(Predicate<Item> constraint, String description, Item... items) {
        return Stream.of(items)
                .filter(constraint)
                .map(i -> i.name + " " + description)
                .collect(joining(", "));
    }
}
