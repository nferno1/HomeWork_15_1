package nferno1.homework_15_1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val wordDAO: WordDAO) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Success)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _error = Channel<String>()
    val error = _error.receiveAsFlow()

    val allWords: StateFlow<List<Word>> = this.wordDAO.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(10000L),
            initialValue = emptyList()
        )


    val allWordsLiveData = this.wordDAO.getAllLiveData()

    suspend fun onAddBtn(newNumber: String) {
        _state.value = State.Loading


        val isValid: Boolean = newNumber.matches(Regex("[a-z|A-Z|-]+"))

        if (!isValid) {
            _state.value = State.Error("Your input is not valid")
            _error.send("Your input is not valid")
        } else {

            val size = allWords.value.size

            var isUpdate = false
            lateinit var word: Word
            var newCntDublcatesValueInt = 1
            allWords.value.forEach {
                if (it.word == newNumber) {
                    isUpdate = true
                    newCntDublcatesValueInt = ++it.cntDublicates
                    word = it.copy(cntDublicates = newCntDublcatesValueInt)
                }
            }
            if (isUpdate) {
                viewModelScope.launch {
                    wordDAO.updateCntDublicates(word)
                }
            } else {
                viewModelScope.launch {
                    wordDAO.insert(NewWord(size, newNumber, newCntDublcatesValueInt))
                }
            }

            _state.value = State.Success
        }
    }

    suspend fun onCleanBtn() {
        allWords.value.forEach { wordDAO.delete(it) }
    }


}