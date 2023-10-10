package com.shine.language

import android.app.Application
import com.shine.language.data.db.DBHelper
import com.shine.language.util.log.i

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        "onCreate()".i()
        instance = this
        DBHelper.init()
    }

    override fun onTerminate() {
        super.onTerminate()
        "onTerminate()".i()
    }

    companion object {
        lateinit var instance: MainApplication
    }
}