package com.gildedrose

import spock.lang.Specification
import spock.lang.Unroll

import static com.gildedrose.GildedRose.AGED_BRIE
import static com.gildedrose.GildedRose.LEGENDARY

class GildedRoseSpec extends Specification {

    @Unroll
    def "don't accept #name goods"() {
        when:
        new GildedRose(new Item(name, sellIn, quality))

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
        def inventory = new GildedRose(new Item('item', 0, 0))

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
        def inventory = new GildedRose(new Item('item', 1, 10))

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
        def inventory = new GildedRose(new Item(LEGENDARY, 1, 1))

        when:
        2.times { inventory.updateQuality() }

        then:
        with(inventory.items.first()) {
            quality == 1
            sellIn == 1
        }
    }

    def 'brie gets better with aging'() {
        given:
        def inventory = new GildedRose(new Item(AGED_BRIE, 1, 49))

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == 50
            sellIn == 0
        }

        when:
        inventory.updateQuality()

        then:
        with(inventory.items.first()) {
            quality == 50
            sellIn == -1
        }
    }
}
