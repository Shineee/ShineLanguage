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
        private var db: LanguageDatabase? = null
        fun getDatabase(): LanguageDatabase? {
            MainApplication.instance?.applicationContext?.let {
                if (db != null)
                    return@let
                db = Room.databaseBuilder(
                    it,
                    LanguageDatabase::class.java, "language.db"
                ).build()
            }
            return db
        }
    }
}