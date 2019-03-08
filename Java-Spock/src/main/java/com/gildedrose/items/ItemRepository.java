package com.gildedrose.items;

import com.gildedrose.Item;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Component
public class ItemRepository {

    private static final String KEY = "inventory";

    private final ListOperations<String, Object> redisOps;

    public ItemRepository(RedisTemplate<String, Object> template) {
        this.redisOps = template.opsForList();
    }

    public void add(Item item) {
        redisOps.leftPush(KEY, new StoredItem(item));
    }

    public Collection<Item> list() {
        Long size = requireNonNull(redisOps.size(KEY));
        return requireNonNull(redisOps.range(KEY, 0, size))
                .stream()
                .map(i -> (StoredItem) i)
                .map(StoredItem::asItem)
                .collect(toList());
    }

    static class StoredItem implements Serializable {

        private String name;
        private int sellIn;
        private int quality;

        StoredItem(Item item) {
            this.name = item.name;
            this.sellIn = item.sellIn;
            this.quality = item.quality;
        }

        Item asItem() {
            return new Item(name, sellIn, quality);
        }
    }
}
