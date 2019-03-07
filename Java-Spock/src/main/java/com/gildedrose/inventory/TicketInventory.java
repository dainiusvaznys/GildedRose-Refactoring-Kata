package com.gildedrose.inventory;

import com.gildedrose.Item;

public class TicketInventory extends InventoryItem {
    public static final String NAME = "Backstage passes to a TAFKAL80ETC concert";

    public TicketInventory(Item item) {
        super(item);
    }

    @Override
    public InventoryItem rollDay() {
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
        item.quality = Math.min(item.quality + by, MAX_QUALITY);
        return this;
    }
}
