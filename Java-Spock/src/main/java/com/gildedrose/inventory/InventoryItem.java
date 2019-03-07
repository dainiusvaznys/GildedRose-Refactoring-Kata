package com.gildedrose.inventory;

import com.gildedrose.Item;

import static java.lang.Math.max;

public class InventoryItem {
    public static final int MAX_QUALITY = 50;

    Item item;

    public InventoryItem(Item item) {
        this.item = item;
    }

    public void rollDay() {
        item.sellIn = item.sellIn - 1;
        degrade(1);
    }

    void degrade(int by) {
        boolean expired = item.sellIn < 0;
        int spoilFactor = expired ? by * 2 : by;
        item.quality = max(item.quality - spoilFactor, 0);
    }
}
