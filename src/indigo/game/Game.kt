package indigo.game

import indigo.deck.Deck
import indigo.output.Output
import indigo.input.Input
import indigo.input.UserAction
import java.util.*

class Game(private val player: Player, private val input: Input, private val output: Output) {

    private val deck = Deck()
    private val computer = Computer(output)

    private var currentPlayer: Player = computer

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer === player) {
            computer
        } else {
            player
        }
    }

    fun playGame() {
        output.display("Indigo Card Game")

        // who shall go first?
        currentPlayer = if (input.getYesOrNo("Play first?") == Input.YES) {
            player
        } else {
            computer
        }

        // initial cards on the table
        deck.get(4)
        output.display(deck.getLastCardOnDiscardPile().toString())


        do {

            when (input.getAction()) {
                UserAction.RESET -> output.display(deck.reset())
                UserAction.SHUFFLE -> output.display(deck.shuffle())
                UserAction.GET -> with(
                    input.getNonNegativeNumberFromRange(
                        1..Deck.MAX_NUMBER_OF_CARDS_IN_DECK,
                        "Number of cards:",
                        "Invalid number of cards."
                    )
                ) {
                    if (this > -1) {
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