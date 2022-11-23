package indigo.game

import indigo.deck.Card
import indigo.input.Input

class Human(name: String, private val input: Input): Player(name = name) {

    fun playCard(): Card {
        val card = input.getNonNegativeNumberFromRange(IntRange(1, getHandSize()), "Choose a card to play (1-${getHandSize()}):")
        return playCard(card)
    }

}