package indigo.input

import indigo.deck.Deck
import indigo.output.Output
import java.util.Scanner

class Input(private val scanner: Scanner, private val output: Output) {

    fun getAction(): UserAction {
        do {
            output.display("Choose an action (reset, shuffle, get, exit):")
            if (scanner.hasNext()) {
                with(UserAction.getByActionText(scanner.next())) {
                    if (this != null) {
                        return this
                    } else {
                        output.display("Wrong action.")
                    }
                }
            }
        } while (true)
    }

    fun getNumberOfCards(): Int {
        output.display("Number of cards:")
        if (scanner.hasNext()) {
            with(scanner.next().toIntOrNull()) {
                if (this != null && this in 1..Deck.MAX_NUMBER_OF_CARDS_IN_DECK) {
                    return this
                } else {
                    output.display("Invalid number of cards.")
                }
            }
        }
        return 0
    }

}