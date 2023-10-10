package com.shine.language.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.shine.language.data.model.English

@Dao
interface EnglishDao {
    @Update
    fun update(english: English)

    @Query("SELECT * FROM tb_english")
    fun getAll(): List<English>

    @Query("SELECT * FROM tb_english")
    fun getAllByPaging(): PagingSource<Int, English>
}