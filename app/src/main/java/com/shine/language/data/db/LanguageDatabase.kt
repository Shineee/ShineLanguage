package com.shine.language.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shine.language.MainApplication
import com.shine.language.data.model.English

@Database(version = 1, entities = [English::class])
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun getEnglishDao(): EnglishDao

    companion object {
        fun getDatabase(): LanguageDatabase {
            return Room.databaseBuilder(
                MainApplication.instance.applicationContext,
                LanguageDatabase::class.java, "language.db"
            ).build()
        }
    }
}