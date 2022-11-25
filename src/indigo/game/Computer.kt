package indigo.game

import indigo.deck.Card
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
        private fun getCardsWithSameSuit(cards: List<Pair<Int, Card>>) = cards
            .groupBy { it.second.suit }
            .filter { it.value.size > 1 }

        /**
         * Finds cards with the same rank.
         * See above, but here for ranks.
         */
        private fun getCardsWithSameRank(cards: List<Pair<Int, Card>>) = cards
            .groupBy { it.second.rank }
            .filter { it.value.size > 1 }

        /**
         * If there are cards in hand with the same suit, throw one of them at random.
         * If there are no cards in hand with the same suit, but there are cards with the same rank, throw one of them at random.
         * If there are no cards in hand with the same suit or rank, then put any card at random.
         */
        fun selectIndexFirstCheckSuitsThenRanksThenRandom(cards: List<Pair<Int, Card>>): Int {
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

        /**
         * Provides candidates - those cards from hand, which can win the cards on table.
         * @param card should be the top card on the table
         * @return list of candidates
         */
        fun provideCandidates(card: Card, cards: List<Pair<Int, Card>>): List<Pair<Int, Card>> = cards
            .stream()
            .filter { it.second.rank == card.rank || it.second.suit == card.suit }
            .toList()

    }

    /**
     * Get index of the card to play from hand.
     * NOTE: Indices used here are of the "computer type" = starting with zero.
     * @param card which is on top of the table (or null when empty)
     * @return index of the card in hand to be played
     */
    private fun getCardToPlay(card: Card?): Int {

        // create an indexed hand, because the logic is built around ranks and suits,
        // but we will need the card's index at the end
        val indexedHand = hand.mapIndexed { index, crd -> Pair(index, crd) }
        output.display(hand.joinToString(" "))

        // condition #1 - only one card in hand
        if (getHandSize() == 1) {
            //println("my hand size is 1, playing what I have")
            return 0
        }

        // condition #3 - no cards on the table (candidate cannot be determined)
        if (card == null) {
            return selectIndexFirstCheckSuitsThenRanksThenRandom(indexedHand)
                //.also { println("no card on table (no candidates), playing $it") }
        }

        // now it's safe to determine candidates (top card is defined)
        val candidates = provideCandidates(card, indexedHand)
        //println("candidates: $candidates")

        // condition #2 - there's only one candidate card, play it
        if (candidates.size == 1) {
            return candidates[0].first
                //.also { println("there's only one candidate card, playing $it") }
        }

        // condition #4 - there are no suitable candidates (same as 3)
        if (candidates.isEmpty()) {
            return selectIndexFirstCheckSuitsThenRanksThenRandom(indexedHand)
                //.also { println("there are no suitable candidates (same as 3), playing $it") }
        }

        // condition #5 - there are candidates, choose one by priority
        return selectIndexFirstCheckSuitsThenRanksThenRandom(candidates)
            //.also { println("there are candidates, choose one by priority, it is $it") }
    }

    override fun playCard(card: Card?): Card {
        return super.playCard(getCardToPlay(card) + 1).also { output.display("$name plays $it") }
    }

}
