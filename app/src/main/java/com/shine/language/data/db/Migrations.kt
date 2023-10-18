package com.shine.language.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shine.language.util.log.i

object Migrations {
    val MIGRATION_0_1 = object : Migration(0, 1) {
        override fun migrate(database: SupportSQLiteDatabase) {
            "start".i()
        }
    }
}