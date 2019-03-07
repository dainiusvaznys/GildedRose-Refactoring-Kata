package com.gildedrose;

class LegendaryInventory extends InventoryItem {
    static final int LEGENDARY_QUALITY = 80;
    static final String NAME = "Sulfuras, Hand of Ragnaros";

    LegendaryInventory(Item item) {
        super(item);

        if (item.quality != LEGENDARY_QUALITY) {
            throw new IllegalArgumentException("invalid " + item.name + " quality");
        }
    }

    @Override
    InventoryItem rollDay() {
        return this;
    }
}
