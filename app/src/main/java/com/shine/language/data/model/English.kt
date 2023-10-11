package com.shine.language.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "tb_english",
    indices = [Index(value = ["word"], unique = true)],
    primaryKeys = ["word"]
)
data class English(
    @ColumnInfo(name = "word") var word: String = "",
    @ColumnInfo(name = "british_accent") var britishAccent: String? = "",
    @ColumnInfo(name = "american_accent") var americanAccent: String? = "",
    @ColumnInfo(name = "explain") var explain: String? = "",
    @ColumnInfo(name = "british_audio") var britishAudio: String? = "",
    @ColumnInfo(name = "american_audio") var americanAudio: String? = ""
)