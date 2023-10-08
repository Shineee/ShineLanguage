package com.shine.language.domain

import com.shine.language.data.datasource.EnglishLocalDataSource
import com.shine.language.data.model.English

class EnglishUseCase {
    private val elds = EnglishLocalDataSource()
    suspend fun getEnglishList(): List<English> {
        return elds.getEnglishList()
    }
}