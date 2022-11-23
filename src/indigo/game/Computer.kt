package indigo.game

import indigo.deck.Card
import indigo.output.Output
import kotlin.random.Random

class Computer(private val output: Output): Player(name = "Computer") {

    fun playCard(): Card {
        val card = playCard(Random.nextInt(0, getHandSize() - 1))
        output.display("$name plays $card")
        return card
    }

}