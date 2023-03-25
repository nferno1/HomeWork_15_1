package nferno1.homework_15_1

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDAO {
    @Query("SELECT * FROM Word LIMIT 5")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT * FROM Word LIMIT 5")
    fun getAllLiveData(): LiveData<List<Word>>

    @Insert(entity = Word::class)
    suspend fun insert(newWord: NewWord)

    @Update
    suspend fun updateCntDublicates(word: Word)

    @Delete
    suspend fun delete(word: Word)


}