package com.gildedrose

import spock.lang.Specification
import spock.lang.Unroll

import static com.gildedrose.InventoryItem.MAX_QUALITY
import static com.gildedrose.LegendaryInventory.LEGENDARY_QUALITY

class GildedRoseSpec extends Specification {

    @Unroll
    def "don't accept #name goods"() {
        when:
        acceptInventory(name, sellIn, quality)

        then:
        def error = thrown(IllegalArgumentException)
        error.message.contains(name)

        where:
        name                | sellIn    | quality
        'spoiled'           | 1         | -1
        'expired'           | -1        | 0
    }

    def 'quality is non-negative past expiration'() {
        given:
        def inventory = acceptInventory('item', 0, 0)

        when:
        2.times { inventory.updateQuality() }

        then:
        with(inventory.items.first()) {
            quality == 0
            sellIn == -2
        }
    }

    def 'quality decreases over time'() {
        given:
        def inventory = acceptInventory('item', 1, 10)

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == 9
            sellIn == 0
        }

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == 7
            sellIn == -1
        }
    }

    def 'legendary goods preserve quality & sell date'() {
        given:
        def inventory = acceptInventory(LegendaryInventory.NAME, 1, LEGENDARY_QUALITY)

        when:
        2.times { inventory.updateQuality() }

        then:
        with(inventory.items.first()) {
            quality == LEGENDARY_QUALITY
            sellIn == 1
        }
    }

    def 'brie gets better with aging'() {
        given:
        def inventory = acceptInventory(AgedBrie.NAME, 1, MAX_QUALITY - 1)

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == MAX_QUALITY
            sellIn == 0
        }

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == MAX_QUALITY
            sellIn == -1
        }
    }

    @Unroll
    def 'passes for #name gain #gain quality'() {
        given:
        def inventory = acceptInventory(TicketInventory.NAME, sellIn, value)

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == value + gain
        }

        where:
        name                            | sellIn    | value             || gain
        'upcoming regular concert'      | 11        | 20                || 1
        'upcoming major concert'        | 11        | MAX_QUALITY - 1   || 1
        'regular concert in 10 days'    | 10        | 20                || 2
        'major concert in 10 days'      | 10        | MAX_QUALITY - 1   || 1
        'regular concert in 5 days'     | 5         | 20                || 3
        'major concert in 5 days'       | 5         | MAX_QUALITY - 1   || 1
        'tomorrow'                      | 1         | 20                || 3
        'today'                         | 0         | 20                || -20
    }

    private static GildedRose acceptInventory(String name, int sellIn, int quality) {
        return new GildedRose(new Item(name, sellIn, quality))
    }
}
