package com.gildedrose;

class LegendaryInventory extends InventoryItem {
    static final String NAME = "Sulfuras, Hand of Ragnaros";

    LegendaryInventory(Item item) {
        super(item);

        if (item.quality != 80) {
            throw new IllegalArgumentException("invalid " + item.name + " quality");
        }
    }

    @Override
    InventoryItem rollDay() {
        return this;
    }
}
