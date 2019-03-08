package com.gildedrose;

import com.gildedrose.items.ItemRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
class ScheduledUpdates {

    private GildedRose inventory;
    private final ItemRepository repository;

    ScheduledUpdates(ItemRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "${app.roll-day-at}")
    void rollDay() {
        Item[] updated = getInventory().updateQuality();
        repository.replaceAll(updated);
    }

    private GildedRose getInventory() {
        if (inventory == null) {
            Item[] items = repository.list().toArray(new Item[0]);
            inventory = new GildedRose(items);
        }

        return inventory;
    }
}
