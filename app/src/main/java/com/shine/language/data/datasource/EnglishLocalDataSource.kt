package com.shine.language.data.datasource

import androidx.paging.PagingSource
import com.shine.language.data.db.LanguageDatabase
import com.shine.language.data.model.English

/**
 * 英语词汇模块相关的本地数据
 */
class EnglishLocalDataSource {
    private val db: LanguageDatabase = LanguageDatabase.getDatabase()

    fun getEnglishList(): PagingSource<Int, English> {
        val dao = db.getEnglishDao()
        return dao.getAllByPaging()
    }
}