package com.gildedrose.items;

import com.gildedrose.Item;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Component
public class ItemRepository {

    private static final String KEY = "inventory";

    private final BoundListOperations<String, Object> redisOps;

    public ItemRepository(RedisTemplate<String, Object> template) {
        redisOps = template.boundListOps(KEY);
    }

    public void add(Item item) {
        redisOps.rightPush(new StoredItem(item));
    }

    public void replaceAll(Item... items) {
        redisOps.getOperations().delete(KEY);

        Stream.of(items)
                .map(StoredItem::new)
                .forEach(i -> requireNonNull(redisOps.rightPush(i)));
    }

    public Collection<Item> list() {
        Long size = requireNonNull(redisOps.size());
        return requireNonNull(redisOps.range(0, size))
                .stream()
                .map(i -> (StoredItem) i)
                .map(StoredItem::asItem)
                .collect(toList());
    }

    // the only reason to duplicate Item here is the restriction to update it
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
