package indigo.game

import indigo.deck.Card
import indigo.deck.Deck
import indigo.deck.Rank
import indigo.output.Output
import indigo.input.Input
import kotlin.random.Random

class Game(private val player: Player, private val input: Input, private val output: Output) {

    companion object {
        const val INITIAL_PILE_SIZE = 4
        const val HAND_DEAL_SIZE = 6
    }

    private val deck = Deck()
    private val computer = Computer()

    private var currentPlayer: Player = computer
    private var firstPlayer: Player = currentPlayer
    private var lastWinner: Player = currentPlayer

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer === player) {
            computer
        } else {
            player
        }
    }

    /**
     * Checks for win condition on the provided deck:
     * If there are at least 2 cards on the pile, checks if their ranks or suits match.
     * If they match, returns the contents of the pile and clears the pile.
     * If there's no match or if the pile has less than 2 card, returns an empty list (= no win)
     * @param deck a game deck
     * @return list of "won" card or empty list if "no win"
     */
    private fun checkWin(deck: Deck): List<Card> {
        with(deck.getTwoTopmostCardFromPileOrEmptyList()) {
            if (this.size == 2) {
                if (this[0].rank == this[1].rank || this[0].suit == this[1].suit) {
                    return deck.getPileAndCleanIt()
                }
            }
        }
        return emptyList()
    }

    /**
     * Calculates score of the provided list of cards
     * @param cards the list of cards to calculate the score from
     * @return the score as a pair: (number of cards, value of cards)
     */
    private fun calculateScore(cards: List<Card>): Pair<Int, Int> {
        var score = 0
        for (card in cards) {
            when(card.rank) {
                Rank.RANK_A, Rank.RANK_10, Rank.RANK_J, Rank.RANK_Q, Rank.RANK_K -> score += 1
                else -> { /* nothing */ }
            }
        }
        return Pair(cards.size, score)
    }

    fun playGame() {
        output.display("Indigo Card Game")

        // who shall go first?
        currentPlayer = if (input.getYesOrNo("Play first?") == Input.YES) {
            player
        } else {
            computer
        }.also {
            firstPlayer = it
            lastWinner = it
        }

        // initial cards on the table
        deck.putOnPile(deck.get(INITIAL_PILE_SIZE))
        output.display("Initial cards on the table: ${deck.getLastCardsOnPileAsString(INITIAL_PILE_SIZE)}")

        // play a game round
        do {
            // check if current player has cards, if needed deal him a hand
            if (currentPlayer.getHandSize() == 0) {
                currentPlayer.populateHand(deck.get(HAND_DEAL_SIZE))
            }

            // display game situation
            output.display(
                if (deck.pileSize() == 0) "No cards on the table"
                else "${deck.pileSize()} cards on the table, and the top card is ${deck.getLastCardOnPileAsString()}")

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
                    deck.putOnPile(listOf(currentPlayer.playCard(cardNum)))
                }
            } else { // if non-interactive (computer) player turn
                val card = currentPlayer.playCard(if (currentPlayer.getHandSize() > 1) Random.nextInt(1, currentPlayer.getHandSize()) else 1)
                output.display("${currentPlayer.name} plays $card")
                deck.putOnPile(listOf(card))
            }

            // check turn winner, calculate score and add to current players score
            with(calculateScore(checkWin(deck))) {
                if (this.first > 0) {
                    output.display("${currentPlayer.name} wins cards")
                    lastWinner = currentPlayer
                    currentPlayer.addScore(this.first, this.second)
                    output.display("Score: ${player.name} ${player.score} - ${computer.name} ${computer.score}")
                    output.display("Cards: ${player.name} ${player.numberOfCardsWon} - ${computer.name} ${computer.numberOfCardsWon}")
                }
            }

            // switch player to next one
            switchPlayer()

            // check whether game has finished
            if (deck.getDeckSize() == 0 && player.getHandSize() == 0 && computer.getHandSize() == 0) {
                // award score from remaining cards on the pile
                val score = calculateScore(deck.getPileAndCleanIt())
                lastWinner.addScore(score.first, score.second)

                // assign extra points to player who won more cards
                if (player.numberOfCardsWon > computer.numberOfCardsWon) player.addScore(0, 3)
                else if (player.numberOfCardsWon < computer.numberOfCardsWon) computer.addScore(0, 3)
                else firstPlayer.addScore(0, 3)

                // break out of loop = game ends
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
        output.display("Score: ${player.name} ${player.score} - ${computer.name} ${computer.score}")
        output.display("Cards: ${player.name} ${player.numberOfCardsWon} - ${computer.name} ${computer.numberOfCardsWon}")
        output.display("Game Over")
    }

}
