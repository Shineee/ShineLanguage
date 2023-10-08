package com.shine.language.data.datasource

import com.shine.language.data.db.LanguageDatabase
import com.shine.language.data.model.English
import java.util.concurrent.Flow

/**
 * 英语词汇模块相关的本地数据
 */
class EnglishLocalDataSource {
    private var db: LanguageDatabase? = null

    constructor() {
        db = LanguageDatabase.getDatabase()
    }

    suspend fun getEnglishList(): List<English> {
        val dao = db?.getEnglishDao()
        return dao?.getAllByLimit().orEmpty()
    }
}