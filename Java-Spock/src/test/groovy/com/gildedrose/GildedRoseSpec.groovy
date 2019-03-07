package com.gildedrose

import spock.lang.Specification
import spock.lang.Unroll

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
        'expired'           | 0         | 0
        'past expiration'   | -1        | 0
    }

    def 'quality is non-negative past expiration'() {
        given:
        def inventory = new GildedRose(new Item('item', 1, 0))

        when:
        2.times { inventory.updateQuality() }

        then:
        with(inventory.items.first()) {
            quality == 0
            sellIn == -1
        }
    }
}
