package com.shine.language.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_english",
    indices = [Index(value = ["word"], unique = true)],
)
data class English(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "word") var word: String = "",
    @ColumnInfo(name = "british_accent") var britishAccent: String? = "",
    @ColumnInfo(name = "american_accent") var americanAccent: String? = "",
    @ColumnInfo(name = "paraphrase") var paraphrase: String? = "",
    @ColumnInfo(name = "british_audio") var britishAudio: String? = "",
    @ColumnInfo(name = "american_audio") var americanAudio: String? = ""
)