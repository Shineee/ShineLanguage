package com.shine.language.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.shine.language.data.model.English
import java.util.concurrent.Flow

@Dao
interface EnglishDao {
    @Update
    fun update(english: English)

    @Query("SELECT * FROM tb_english")
    fun getAll(): List<English>
    @Query("SELECT * FROM tb_english LIMIT 40")
    suspend fun getAllByLimit(): List<English>
}