package com.gildedrose

import spock.lang.Specification

import static com.gildedrose.GildedRose.AGED_BRIE
import static com.gildedrose.GildedRose.BACKSTAGE_PASSES
import static com.gildedrose.GildedRose.LEGENDARY

class GildedRoseSpec extends Specification {

    def "don't accept spoiled goods"() {
        given:
        def spoiled = new Item('item1', 0, -1)

        when:
        new GildedRose(spoiled).updateQuality()

        then:
        thrown(IllegalArgumentException)
    }

    def 'quality is non-negative past expiration'() {

        given:
        def items = ['item1', AGED_BRIE, BACKSTAGE_PASSES, LEGENDARY]
            .collect { new Item(it, 0, 0) }

        when:
        def updated = new GildedRose(items).updateQuality()

        then:
        updated.each { it.quality == 0 }
        updated.each { it.sellIn == -1 }
    }
}
