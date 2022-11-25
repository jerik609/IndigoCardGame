package indigo.input

enum class UserAction(private val actionText: String) {
    RESET("reset"),
    SHUFFLE("shuffle"),
    GET("get"),
    EXIT("exit"),
    PRINT_INTERNALS("int"), ;

    companion object {

        fun getByActionText(actionText: String): UserAction? {
            for (enum in UserAction.values()) {
                if (enum.actionText == actionText) return enum
            }
            return null
        }

    }
}
