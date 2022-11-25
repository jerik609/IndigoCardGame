package indigo.game

import indigo.deck.Card
import indigo.input.Input
import indigo.output.Output

class Human(name: String, private val input: Input, private val output: Output): Player(name = name) {

    override fun playCard(card: Card?): Card? {
        output.display("Cards in hand: ${cardsInHandAsString()}")
        val cardNum = input.getNonNegativeNumberFromRange(
            1..getHandSize(),
            "Choose a card to play (1-${getHandSize()}):"
        )
        if (cardNum == Input.EXIT_SIGNAL) {
            return null
        }
        return super.playCard(cardNum)
    }

}
