package indigo.game

import indigo.deck.Deck
import indigo.output.Output
import indigo.input.Input
import indigo.input.UserAction
import java.util.*
import kotlin.random.Random

class Game(private val player: Player, private val input: Input, private val output: Output) {

    companion object {
        const val INITIAL_DISCARD_PILE_SIZE = 4
        const val HAND_DEAL_SIZE = 6
    }

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
        deck.putOnDiscardPile(deck.get(INITIAL_DISCARD_PILE_SIZE))
        output.display("Initial cards on the table: ${deck.getLastCardsOnDiscardPileAsString(INITIAL_DISCARD_PILE_SIZE)}")

        // play a game round
        do {
            // check if current player has cards, if needed deal him a hand
            if (currentPlayer.getHandSize() == 0) {
                currentPlayer.populateHand(deck.get(HAND_DEAL_SIZE))
            }

            // display game situation
            output.display("${deck.getDiscardPileSize()} cards on the table, and the top card is ${deck.getLastCardOnDiscardPileAsString()}")

            // current player plays a card
            if (currentPlayer.isInteractive()) { // if (interactive) player's turn
                output.display("Cards in hand: ${currentPlayer.cardsInHandAsString()}")
                val cardNum = input.getNonNegativeNumberFromRange(
                    1..currentPlayer.getHandSize(),
                    "Choose a card to play (1-${currentPlayer.getHandSize()}):"
                )
                if (cardNum == Input.EXIT_SIGNAL) {
                    break
                } else {
                    deck.putOnDiscardPile(listOf(currentPlayer.playCard(cardNum)))
                }
            } else { // if non-interactive (computer) player turn
                val card = currentPlayer.playCard(if (currentPlayer.getHandSize() > 1) Random.nextInt(1, currentPlayer.getHandSize()) else 1)
                output.display("${currentPlayer.name} plays $card")
                deck.putOnDiscardPile(listOf(card))
            }

            // switch player to next one
            switchPlayer()

            // check game finished
            if (deck.getDeckSize() == 0 && player.getHandSize() == 0 && computer.getHandSize() == 0) {
                output.display("${Deck.MAX_NUMBER_OF_CARDS_IN_DECK} cards on the table, and the top card is ${deck.getLastCardOnDiscardPileAsString()}")
                break
            }

//            when (input.getAction()) {
//                UserAction.RESET -> output.display(deck.reset())
//                UserAction.SHUFFLE -> output.display(deck.shuffle())
//                UserAction.GET -> with(
//                    input.getNonNegativeNumberFromRange(
//                        1..Deck.MAX_NUMBER_OF_CARDS_IN_DECK,
//                        "Number of cards:",
//                        "Invalid number of cards."
//                    )
//                ) {
//                    if (this > -1) {
//                        with(deck.get(this)) {
//                            if (this.isEmpty()) {
//                                output.display("The remaining cards are insufficient to meet the request.")
//                            } else {
//                                output.display(this.joinToString(" "))
//                            }
//                        }
//                    }
//                }
//                UserAction.EXIT -> {
//                    output.display("Bye")
//                    return
//                }
//                UserAction.PRINT_INTERNALS -> output.display(deck.toString())
//            }


        } while(true)
        output.display("Game Over")
    }

}