package com.gildedrose;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

class GildedRose {
    static final String AGED_BRIE = "Aged Brie";
    static final String BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";
    static final String LEGENDARY = "Sulfuras, Hand of Ragnaros";

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

    Item[] updateQuality() {
        Stream.of(items).forEach(this::rollDay);
        return items;
    }

    private void rollDay(Item item) {
        if (item.name.equals(AGED_BRIE) || item.name.equals(BACKSTAGE_PASSES)) {
            if (item.quality < 50) {
                item.quality = item.quality + 1;

                if (item.name.equals(BACKSTAGE_PASSES)) {
                    if (item.sellIn <= 10 && item.quality < 50) {
                        item.quality = item.quality + 1;
                    }

                    if (item.sellIn <= 5 && item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        } else if (item.quality > 0 && !item.name.equals(LEGENDARY)) {
            item.quality = item.quality - 1;
        }

        if (!item.name.equals(LEGENDARY)) {
            item.sellIn = item.sellIn - 1;
        }

        if (item.sellIn >= 0) {
            return;
        }

        if (item.name.equals(AGED_BRIE)) {
            if (item.quality < 50) {
                item.quality = item.quality + 1;
            }

            return;
        }

        if (item.name.equals(BACKSTAGE_PASSES)) {
            item.quality = 0;
            return;
        }

        if (item.quality > 0 && !item.name.equals(LEGENDARY)) {
            item.quality = item.quality - 1;
        }
    }

    private static String filterInventory(Predicate<Item> constraint, String description, Item... items) {
        return Stream.of(items)
                .filter(constraint)
                .map(i -> i.name + " " + description)
                .collect(joining(", "));
    }
}
