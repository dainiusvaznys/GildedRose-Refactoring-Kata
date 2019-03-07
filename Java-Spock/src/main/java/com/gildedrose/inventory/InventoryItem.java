package com.gildedrose.inventory;

import com.gildedrose.Item;

import static java.lang.Math.max;

public class InventoryItem {
    public static final int MAX_QUALITY = 50;

    protected Item item;

    public InventoryItem(Item item) {
        this.item = item;
    }

    public void rollDay() {
        item.sellIn = item.sellIn - 1;

        boolean expired = item.sellIn < 0;
        int spoilFactor = expired ? 2 : 1;
        item.quality = max(item.quality - spoilFactor, 0);

    }
}
