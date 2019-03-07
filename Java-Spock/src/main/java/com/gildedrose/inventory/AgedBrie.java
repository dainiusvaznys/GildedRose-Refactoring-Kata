package com.gildedrose.inventory;

import com.gildedrose.Item;

import static java.lang.Math.min;

public class AgedBrie extends InventoryItem {
    public static final String NAME = "Aged Brie";

    public AgedBrie(Item item) {
        super(item);
    }

    @Override
    public InventoryItem rollDay() {
        item.sellIn = item.sellIn - 1;
        item.quality = min(item.quality + 1, MAX_QUALITY);

        return this;
    }
}
