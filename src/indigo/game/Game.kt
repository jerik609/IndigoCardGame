package indigo.game

import indigo.deck.Deck
import indigo.output.Output
import indigo.input.Input
import indigo.input.UserAction
import java.util.*

class Game {

    private val deck = Deck()
    private val output = Output()
    private val input = Input(Scanner(System.`in`), output)

    fun playGame() {
        do {
            when (input.getAction()) {
                UserAction.RESET -> output.display(deck.reset())
                UserAction.SHUFFLE -> output.display(deck.shuffle())
                UserAction.GET -> with(input.getNumberOfCards()) {
                    if (this > 0) {
                        with(deck.get(this)) {
                            if (this.isEmpty()) {
                                output.display("The remaining cards are insufficient to meet the request.")
                            } else {
                                output.display(this.joinToString(" "))
                            }
                        }
                    }
                }
                UserAction.EXIT -> {
                    output.display("Bye")
                    return
                }
                UserAction.PRINT_INTERNALS -> output.display(deck.toString())
            }
        } while(true)
    }

}