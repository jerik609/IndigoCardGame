package indigo

import indigo.game.Game
import indigo.game.Human
import indigo.input.Input
import indigo.output.Output
import java.util.*

fun main() {
    val output = Output()
    val input = Input(Scanner(System.`in`), output)
    val game = Game(Human("Player", input, output), input, output)
    game.playGame()
}
