package indigo.deck

data class Card(val suit: Suit, val rank: Rank) {

    override fun toString(): String {
        return "${rank.value}${suit.value}"
    }

}
