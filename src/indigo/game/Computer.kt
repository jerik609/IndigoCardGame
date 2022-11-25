package indigo.game

import indigo.output.Output

class Computer(): Player(name = "Computer") {

    override fun isInteractive() = false

}
