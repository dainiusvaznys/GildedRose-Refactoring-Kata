package com.gildedrose

import spock.lang.Specification

class GildedRoseSpec extends Specification {

    def "don't accept spoiled goods"() {
        given:
        def spoiled = new Item('item1', 0, -1)

        when:
        new GildedRose(spoiled).updateQuality()

        then:
        thrown(IllegalArgumentException)
    }
}
