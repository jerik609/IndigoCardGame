package indigo.game

import indigo.deck.Card

abstract class Player(val name: String) {
    private val hand = mutableListOf<Card>()

    fun getHandSize(): Int {
        return hand.size
    }

    fun playCard(number: Int): Card {
        check(hand.isNotEmpty())
        return hand.removeAt(number - 1)
    }

    fun populateHand(cards: List<Card>) {
        hand.addAll(cards)
    }
}