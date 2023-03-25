package nferno1.homework_15_1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewWord(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo(name = "word")
    val word: String,
    @ColumnInfo(name = "count_of_dublicates")
    val cntDublicates: Int
)