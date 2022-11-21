package indigo.input

import com.sun.org.slf4j.internal.LoggerFactory
import indigo.output.Output
import java.util.Scanner

class Input(private val scanner: Scanner, private val output: Output) {

    companion object {
        private val log = LoggerFactory.getLogger(Input::class.java)
    }

    fun getAction(): UserAction {
        do {
            output.display("Choose an action (reset, shuffle, get, exit):")
            if (scanner.hasNext()) {
                val input = scanner.next()
                log.debug("getAction input: {}", input)
                with(UserAction.getByActionText(input)) {
                    if (this != null) return this else println("Wrong action.")
                }
            }
        } while (true)
    }

    fun getNumberOfCards(): Int {
        do {
            output.display("Number of cards:")
            if (scanner.hasNextInt()) {
                val input = scanner.nextInt()
                log.debug("getNumberOfCards input: {}", input)
                return input
            }
        } while (true)
    }

}