package indigo.input

import indigo.output.Output
import java.util.Scanner

class Input(private val scanner: Scanner, private val output: Output) {

    companion object {
        const val EXIT_SIGNAL = Int.MIN_VALUE
        const val YES = "yes"
        const val NO = "no"
        const val EXIT = "exit"
    }

//    fun getAction(): UserAction {
//        do {
//            output.display("Choose an action (reset, shuffle, get, exit):")
//            if (scanner.hasNext()) {
//                with(UserAction.getByActionText(scanner.next())) {
//                    if (this != null) {
//                        return this
//                    } else {
//                        output.display("Wrong action.")
//                    }
//                }
//            }
//        } while (true)
//    }

    fun getYesOrNo(prompt: String, failMsg: String = ""): String {
        do {
            output.display(prompt)
            if (scanner.hasNext()) {
                with(scanner.next()) {
                    when (this) {
                        YES, NO -> return this
                        else -> if (failMsg.isNotEmpty()) output.display(failMsg)
                    }
                }
            }
        } while (true)
    }

    fun getNonNegativeNumberFromRange(range: IntRange, prompt: String, failMsg: String = ""): Int {
        require(range.first >= 0)
        do {
            output.display(prompt)
            if (scanner.hasNext()) {
                val input = scanner.next()
                if (input == EXIT) return EXIT_SIGNAL
                with(input.toIntOrNull()) {
                    if (this != null && this in range) {
                        return this
                    } else {
                        if (failMsg.isNotEmpty()) output.display("Invalid number of cards.")
                    }
                }
            }
        } while (true)
    }
}
