package indigo.game

import indigo.deck.Card

abstract class Player(val name: String) {
    var numberOfCardsWon: Int = 0
        private set
    var score: Int = 0
        private set
    protected val hand = mutableListOf<Card>() //TODO: should really use own class for The Hand :-)

    fun getHandSize(): Int {
        return hand.size
    }

    fun cardsInHandAsString(): String {
        check(hand.isNotEmpty())
        return "1)${hand.map { it.toString() }.reduceIndexed { index, accumulator, item -> "$accumulator ${index + 1})$item" }}"
    }

    abstract fun playCard(card: Card?): Card?

    protected fun playCard(number: Int): Card {
        check(hand.isNotEmpty())
        return hand.removeAt(number - 1)
    }

    fun populateHand(cards: List<Card>) {
        hand.addAll(cards)
    }

    fun resetScore() {
        numberOfCardsWon = 0
        score = 0
    }

    fun addScore(numberOfCardsWon: Int, score: Int) {
        this.numberOfCardsWon += numberOfCardsWon
        this.score += score
    }

}
