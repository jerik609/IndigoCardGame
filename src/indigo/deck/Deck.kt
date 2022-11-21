package indigo.deck

class Deck {

    companion object {
        const val MAX_NUMBER_OF_CARDS_IN_DECK = 52
    }

    private val deck = mutableListOf<Card>()
    private val discardPile = mutableListOf<Card>()

    init {
        reset()
    }

    override fun toString(): String {
        return deck.joinToString(" ")
    }

    /**
     * Removes the current contents of the deck and populates it with fresh set of 52 cards.
     */
    fun reset(): String {
        deck.clear()
        discardPile.clear()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                deck.add(Card(suit = suit, rank = rank))
            }
        }
        shuffle()
        return "Card deck is reset."
    }

    /**
     * Shuffles the current contents of the deck.
     */
    fun shuffle(): String {
        deck.shuffle()
        return "Card deck is shuffled."
    }

    /**
     * Gets a card from deck - removes it from the deck and puts it on the discard pile.
     * @param numberOfCards max number of cards to be taken from the deck
     * @return list of cards taken from deck
     */
    fun get(numberOfCards: Int): List<Card> {
        require(numberOfCards in 1..MAX_NUMBER_OF_CARDS_IN_DECK)
        return if (deck.size < numberOfCards) {
            emptyList()
        } else {
            deck.take(numberOfCards).also { discardPile.addAll(it) }
        }
    }

}