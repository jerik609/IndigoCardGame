package indigo.game

import indigo.deck.Card
import indigo.output.Output
import kotlin.random.Random

class Computer(private val output: Output): Player(name = "Computer") {

    override fun isInteractive() = false

}