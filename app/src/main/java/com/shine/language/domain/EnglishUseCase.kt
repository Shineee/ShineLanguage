package com.shine.language.domain

import androidx.paging.PagingSource
import com.shine.language.data.datasource.EnglishLocalDataSource
import com.shine.language.data.model.English

class EnglishUseCase {
    private val elds = EnglishLocalDataSource()
    fun getEnglishList(): PagingSource<Int, English> {
        return elds.getEnglishList()
    }
}