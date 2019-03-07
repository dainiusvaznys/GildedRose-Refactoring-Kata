package com.gildedrose;

import static java.lang.Math.min;

class AgedBrie extends InventoryItem {
    static final String NAME = "Aged Brie";

    AgedBrie(Item item) {
        super(item);
    }

    @Override
    InventoryItem rollDay() {
        item.sellIn = item.sellIn - 1;
        item.quality = min(item.quality + 1, 50);

        return this;
    }
}
