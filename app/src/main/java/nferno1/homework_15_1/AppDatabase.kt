package nferno1.homework_15_1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Word::class, NewWord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDAO
}