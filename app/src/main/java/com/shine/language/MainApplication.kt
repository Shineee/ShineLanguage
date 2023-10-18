package com.shine.language

import android.app.Application
import com.shine.language.util.log.i
import com.tencent.mmkv.MMKV

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        "onCreate()".i()
        instance = this
        MMKV.initialize(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        "onTerminate()".i()
    }

    companion object {
        lateinit var instance: MainApplication
    }
}