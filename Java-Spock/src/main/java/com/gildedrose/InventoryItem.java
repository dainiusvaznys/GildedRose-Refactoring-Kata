package com.gildedrose;

import static java.lang.Math.max;

class InventoryItem {
    protected Item item;

    InventoryItem(Item item) {
        this.item = item;
    }

    InventoryItem rollDay() {
        item.sellIn = item.sellIn - 1;

        boolean expired = item.sellIn < 0;
        int spoilFactor = expired ? 2 : 1;
        item.quality = max(item.quality - spoilFactor, 0);

        return this;
    }
}
