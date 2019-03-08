package com.gildedrose.inventory;

import com.gildedrose.Item;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class InventoryItem {
    public static final int MAX_QUALITY = 50;

    Item item;

    public InventoryItem(Item item) {
        this.item = item;
    }

    public void rollDay() {
        item.sellIn = item.sellIn - 1;
        decreaseWorth(1);
    }

    void increaseWorth(int by) {
        item.quality = min(item.quality + by, MAX_QUALITY);
    }

    void decreaseWorth(int by) {
        boolean expired = item.sellIn < 0;
        int degradeFactor = expired ? by * 2 : by;
        item.quality = max(item.quality - degradeFactor, 0);
    }
}
