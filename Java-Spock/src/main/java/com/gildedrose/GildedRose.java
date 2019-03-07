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
        if (!item.name.equals(AGED_BRIE)
                && !item.name.equals(BACKSTAGE_PASSES)) {
            if (item.quality > 0) {
                if (!item.name.equals(LEGENDARY)) {
                    item.quality = item.quality - 1;
                }
            }
        } else {
            if (item.quality < 50) {
                item.quality = item.quality + 1;

                if (item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                    if (item.sellIn < 11) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }

                    if (item.sellIn < 6) {
                        if (item.quality < 50) {
                            item.quality = item.quality + 1;
                        }
                    }
                }
            }
        }

        if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
            item.sellIn = item.sellIn - 1;
        }

        if (item.sellIn < 0) {
            if (!item.name.equals("Aged Brie")) {
                if (!item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                    if (item.quality > 0) {
                        if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
                            item.quality = item.quality - 1;
                        }
                    }
                } else {
                    item.quality = item.quality - item.quality;
                }
            } else {
                if (item.quality < 50) {
                    item.quality = item.quality + 1;
                }
            }
        }
    }

    private static String filterInventory(Predicate<Item> constraint, String description, Item... items) {
        return Stream.of(items)
                .filter(constraint)
                .map(i -> i.name + " " + description)
                .collect(joining(", "));
    }
}
