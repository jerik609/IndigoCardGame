package indigo.game

class Human(name: String): Player(name = name) {

    override fun isInteractive() = true

}
