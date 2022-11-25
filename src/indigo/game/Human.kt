package indigo.game

import indigo.deck.Card
import indigo.input.Input

class Human(name: String): Player(name = name) {

    override fun isInteractive() = true

}
