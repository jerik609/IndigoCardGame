package indigo.game

import indigo.deck.Card

abstract class Player(val name: String) {
    private val hand = mutableListOf<Card>()

    fun getHandSize(): Int {
        return hand.size
    }

    fun cardsInHandAsString(): String {
        check(hand.isNotEmpty())
        return "1)${hand.map { it.toString() }.reduceIndexed { index, accumulator, item -> "$accumulator ${index + 1})$item" }}"
    }

    open fun playCard(number: Int): Card {
        check(hand.isNotEmpty())
        return hand.removeAt(number - 1)
    }

    fun populateHand(cards: List<Card>) {
        hand.addAll(cards)
    }

    abstract fun isInteractive(): Boolean
}