package com.shine.language.util.log

import android.util.Log
import com.shine.language.BuildConfig

object L {
    private val isDebug = BuildConfig.DEBUG
    fun d(tag: String, msg: String) {
        if (!isDebug) return
        Log.d(tag, msg)
    }

    fun w(tag: String, msg: String) {
        if (!isDebug) return
        Log.w(tag, msg)
    }

    fun e(tag: String, msg: String, t: Throwable) {
        if (!isDebug) return
        Log.e(tag, msg, t)
    }
}