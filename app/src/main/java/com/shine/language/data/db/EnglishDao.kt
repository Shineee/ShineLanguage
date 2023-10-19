package com.shine.language.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.shine.language.data.model.English

@Dao
interface EnglishDao {
    @Query("SELECT id,word,british_accent,american_accent,paraphrase,british_audio,american_audio FROM tb_english")
    fun getAllByPaging(): PagingSource<Int, English>
}