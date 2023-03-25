package nferno1.homework_15_1

sealed class State {

    object Loading : State()
    object Success : State()
    data class Error(val msg: String) : State()

}