package com.shine.language.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shine.language.MainApplication
import com.shine.language.data.model.English
import com.shine.language.util.DataStore
import com.tencent.mmkv.MMKV

@Database(version = 1, entities = [English::class])
abstract class LanguageDatabase : RoomDatabase() {
    abstract fun getEnglishDao(): EnglishDao

    companion object {
        fun getDatabase(): LanguageDatabase {
            val context = MainApplication.instance.applicationContext
            val isCreateDB = MMKV.defaultMMKV().getBoolean(DataStore.isCreateDB, false)
            val db = if (isCreateDB) {
                Room.databaseBuilder(
                    context,
                    LanguageDatabase::class.java, "language.db"
                ).build()
            } else {
                MMKV.defaultMMKV().putBoolean(DataStore.isCreateDB, true)
                Room.databaseBuilder(
                    context,
                    LanguageDatabase::class.java, "language.db"
                ).createFromAsset("language.db").build()
            }
            return db
        }
    }
}