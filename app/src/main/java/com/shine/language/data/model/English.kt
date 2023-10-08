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
    @ColumnInfo(name = "word") val word: String = "",
    @ColumnInfo(name = "british_accent") val britishAccent: String? = "",
    @ColumnInfo(name = "american_accent") val americanAccent: String? = "",
    @ColumnInfo(name = "explain") val explain: String? = "",
    @ColumnInfo(name = "british_audio") val britishAudio: String? = "",
    @ColumnInfo(name = "american_audio") val americanAudio: String? = ""
)