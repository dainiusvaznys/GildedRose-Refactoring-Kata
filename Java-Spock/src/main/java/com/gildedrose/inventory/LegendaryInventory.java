package com.gildedrose.inventory;

import com.gildedrose.Item;

public class LegendaryInventory extends InventoryItem {
    public static final int LEGENDARY_QUALITY = 80;
    public static final String NAME = "Sulfuras, Hand of Ragnaros";

    public LegendaryInventory(Item item) {
        super(item);

        if (item.quality != LEGENDARY_QUALITY) {
            throw new IllegalArgumentException("invalid " + item.name + " quality");
        }
    }

    @Override
    public void rollDay() {
    }
}
