package com.gildedrose;

import com.gildedrose.inventory.AgedBrie;
import com.gildedrose.inventory.ConjuredInventory;
import com.gildedrose.inventory.LegendaryInventory;
import com.gildedrose.inventory.Tickets;
import com.gildedrose.items.ItemRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.stream.IntStream;

import static com.gildedrose.inventory.InventoryItem.MAX_QUALITY;
import static com.gildedrose.inventory.LegendaryInventory.LEGENDARY_QUALITY;

@SpringBootApplication
@Configuration
class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

    @Bean
    ApplicationListener<ContextRefreshedEvent> contextRefreshed() {
        return event -> {
            ItemRepository repository = event.getApplicationContext()
                    .getAutowireCapableBeanFactory()
                    .getBean(ItemRepository.class);
            populateTestData(repository);
        };
    }

    private void populateTestData(ItemRepository repository) {
        TestDataGenerator generator = new TestDataGenerator();

        IntStream.range(0, 10)
                .mapToObj(i -> generator.item())
                .forEach(repository::add);
        repository.add(generator.item(AgedBrie.NAME, generator.randomQuality(MAX_QUALITY)));
        repository.add(generator.item(ConjuredInventory.NAME, generator.randomQuality(MAX_QUALITY)));
        repository.add(generator.item(Tickets.NAME, generator.randomQuality(MAX_QUALITY)));
        repository.add(generator.item(LegendaryInventory.NAME, LEGENDARY_QUALITY));
    }

    private static class TestDataGenerator {
        private final Faker faker = new Faker();

        private Item item() {
            return item(faker.commerce().productName(), randomQuality(MAX_QUALITY));
        }

        private Item item(String name, int quality) {
            return new Item(name, randomSellIn(), quality);
        }

        private int randomSellIn() {
            return faker.number().numberBetween(10, 20);
        }

        private int randomQuality(int maxValue) {
            return faker.number().numberBetween(0, maxValue);
        }
    }
}
