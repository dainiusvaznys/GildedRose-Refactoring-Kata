package com.gildedrose;

import java.util.Collection;
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
        for (int i = 0; i < items.length; i++) {
            if (!items[i].name.equals(AGED_BRIE)
                    && !items[i].name.equals(BACKSTAGE_PASSES)) {
                if (items[i].quality > 0) {
                    if (!items[i].name.equals(LEGENDARY)) {
                        items[i].quality = items[i].quality - 1;
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].name.equals("Aged Brie")) {
                    if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].quality > 0) {
                            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }

        return items;
    }

    private static String filterInventory(Predicate<Item> constraint, String description, Item... items) {
        return Stream.of(items)
                .filter(constraint)
                .map(i -> i.name + " " + description)
                .collect(joining(", "));
    }
}
