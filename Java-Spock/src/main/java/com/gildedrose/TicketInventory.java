package com.gildedrose;

import static java.lang.Math.min;

class TicketInventory extends InventoryItem {
    static final String NAME = "Backstage passes to a TAFKAL80ETC concert";

    TicketInventory(Item item) {
        super(item);
    }

    @Override
    InventoryItem rollDay() {
        item.sellIn = item.sellIn - 1;

        if (item.sellIn < 0) {
            item.quality = 0;
            return this;
        }
        if (item.sellIn < 5) {
            return increaseWorth(3);
        }
        if (item.sellIn < 10) {
            return increaseWorth(2);
        }

        return increaseWorth(1);
    }

    private InventoryItem increaseWorth(int by) {
        item.quality = min(item.quality + by, MAX_QUALITY);
        return this;
    }
}
