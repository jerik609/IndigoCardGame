package indigo.game

import indigo.deck.Card
import indigo.deck.Deck
import indigo.output.Output

class Computer(private val output: Output): Player(name = "Computer") {

    companion object {

        /**
         * Finds cards with the same suit.
         * We start with a list of indexed cards = List<Pair<Int, Card>>, then we:
         * 1. groups the pairs in the list by card's suits => we get a Map(Key = suit, Value = List of Pairs)
         * 2. we filter those map entries, where the List of Pairs has 2 and more elements
         * @return Map<Suit, List<Pair<Int, Card>> where list has 2 or more elements
         */
        fun getCardsWithSameSuit(cards: List<Pair<Int, Card>>) = cards
            .groupBy { it.second.suit }
            .filter { it.value.size > 1 }

        /**
         * Finds cards with the same rank.
         * See above, but here for ranks.
         */
        internal fun getCardsWithSameRank(cards: List<Pair<Int, Card>>) = cards
            .groupBy { it.second.rank }
            .filter { it.value.size > 1 }

        /**
         * If there are cards in hand with the same suit, throw one of them at random.
         * If there are no cards in hand with the same suit, but there are cards with the same rank, throw one of them at random.
         * If there are no cards in hand with the same suit or rank, then put any card at random.
         */
        internal fun selectIndexFirstCheckSuitsThenRanksThenRandom(cards: List<Pair<Int, Card>>): Int {
            val cardsWithTheSameSuit = getCardsWithSameSuit(cards)
            val cardsWithTheSameRank = getCardsWithSameRank(cards)

            return if (cardsWithTheSameSuit.isNotEmpty()) {
                cardsWithTheSameSuit.entries.random().value.random().first
            } else if (cardsWithTheSameRank.isNotEmpty()) {
                cardsWithTheSameRank.entries.random().value.random().first
            } else {
                cards.mapIndexed { idx, crd -> Pair(idx, crd) }.random().first
            }
        }

    }

    /**
     * Provides candidates - those cards from hand, which can win the cards on table.
     * @param card should be the top card on the table
     * @return list of candidates
     */
    internal fun provideCandidates(card: Card) = hand
        .mapIndexed { idx, crd -> Pair(idx, crd) }
        .stream()
        .filter { it.second.rank == card.rank || it.second.suit == card.suit }
        .toList()

    /**
     * Get index of the card to play from hand.
     * @param card which is on top of the table (or null when empty)
     * @return index of the card in hand to be played
     */
    private fun getCardToPlay(card: Card?): Int {
        val indexedHand = hand.mapIndexed { index, crd -> Pair(index, crd) }

        // condition #1 - only one card in hand
        if (getHandSize() == 1) {
            return 1
        }

        // condition #3 - no cards on the table (candidate cannot be determined)
        if (card == null) {
            return selectIndexFirstCheckSuitsThenRanksThenRandom(indexedHand)
        }

        // now it's safe to determine candidates (top card is defined)
        val candidates = provideCandidates(card)

        // condition #2 - there's only one candidate card, play it
        if (candidates.size == 1) {
            return candidates[0].first
        }

        // condition #4 - there are no suitable candidates (same as 3)
        if (candidates.isEmpty()) {
            return selectIndexFirstCheckSuitsThenRanksThenRandom(indexedHand)
        }

        // condition #5 - we have a valid hand
        return selectIndexFirstCheckSuitsThenRanksThenRandom(candidates)
    }

    fun getTheHand() = hand

    override fun playCard(card: Card?): Card {
        output.display("$name plays $card")
        return super.playCard(getCardToPlay(card) + 1)
    }

}

//TODO: debug, can be deleted, but I'll keep it to track my reasoning
fun main() {
    val computer = Computer(Output())
    val deck = Deck()
    computer.populateHand(deck.get(10))

    println(computer.cardsInHandAsString())
    println()

    val indexedHand = computer.getTheHand().mapIndexed { idx, crd -> Pair(idx, crd) }

    val suits = Computer.getCardsWithSameSuit(indexedHand)
    println(suits)
    println(suits.entries.random().value.random())
    println()

    val ranks = Computer.getCardsWithSameRank(indexedHand)
    println(ranks)
    println(ranks.entries.random().value.random())
    println()

    val card = deck.get(1)[0]
    val candidates = computer.provideCandidates(card)
    println("Candidates for: $card")
    println(candidates)
    println()
}
