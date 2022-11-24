package indigo.game

import indigo.deck.Card
import indigo.input.Input

class Human(name: String, private val input: Input): Player(name = name) {

    override fun isInteractive() = true

}