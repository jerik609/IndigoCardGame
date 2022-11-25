package indigo.deck

class Deck {

    companion object {
        const val MAX_NUMBER_OF_CARDS_IN_DECK = 52
    }

    private val deck = mutableListOf<Card>()
    private val table = mutableListOf<Card>()

    init {
        reset()
    }

    override fun toString(): String {
        return "Deck(Size: ${deck.size}, Contents: [${deck.joinToString(" ")}]\n" +
                "Table(Size: ${table.size}, Contents: [${table.joinToString(" ")}]"
    }

    /**
     * Removes the current contents of the deck and populates it with fresh set of 52 cards.
     */
    fun reset(): String {
        deck.clear()
        table.clear()
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
     * Returns a number of topmost cards from deck and removes them from the deck.
     * @param numberOfCards max number of cards to be taken from the deck
     * @return list of cards taken from deck
     */
    fun get(numberOfCards: Int): List<Card> {
        require(numberOfCards in 1..MAX_NUMBER_OF_CARDS_IN_DECK)
        return if (deck.size < numberOfCards) {
            emptyList()
        } else {
            val removed = mutableListOf<Card>()
            for (elem in 1..numberOfCards) {
                removed.add(deck.removeFirst())
            }
            return removed
        }
    }

    fun getDeckSize() = deck.size

    fun putOnTable(cards: List<Card>) = table.addAll(cards)

    fun tableSize() = table.size

    /**
     * Get topmost card on table.
     * @return the topmost card on table
     */
    fun getTopmostCardOnTable() = table.lastOrNull()

    /**
     * Display topmost card on table.
     * Used in output.
     * @return string representation of the topmost card on the table
     */
    fun getTopmostCardOnTableAsString() = getTopmostCardsOnTableAsString(1)

    /**
     * Return what's on the table in a string representation.
     * Used in output.
     * @return string representation of the table
     */
    fun getTopmostCardsOnTableAsString(number: Int) = table.takeLast(number).joinToString(" ")

    /**
     * Return the two topmost cards from table.
     * Used when evaluating if the last move has made the player wins the cards on the table.
     * @return list of two topmost cards on table
     */
    fun getTwoTopmostCardFromTableOrEmptyList(): List<Card> = if (table.size < 2) emptyList() else table.takeLast(2)

    /**
     * Return all cards from the table and remove everything from table.
     * This is used when a player has won cards or game has finished, and we are awarding the remaining cards.
     * @return list of cards which were on the table
     */
    fun getTableAndCleanIt(): List<Card> {
        return table.take(tableSize()).also { table.clear() }
    }

}
