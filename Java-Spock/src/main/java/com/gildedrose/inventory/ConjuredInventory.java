package com.gildedrose.inventory;

import com.gildedrose.Item;

public class ConjuredInventory extends InventoryItem {
    public static final String NAME = "Conjured inventory";

    public ConjuredInventory(Item item) {
        super(item);
    }

    @Override
    public void rollDay() {
        item.sellIn = item.sellIn - 1;
        degrade(2);
    }
}
