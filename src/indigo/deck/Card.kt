package indigo.deck

data class Card(private val suit: Suit, private val rank: Rank) {

    override fun toString(): String {
        return "${rank.value}${suit.value}"
    }

}