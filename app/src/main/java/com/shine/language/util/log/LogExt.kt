package com.shine.language.util.log

import android.util.Log

const val TAG = "Shine"
fun String.i() {
    val message = "${getPrefix()} $this"
    Log.i(TAG, message)
}

fun String.w() {
    val message = "${getPrefix()} $this"
    Log.w(TAG, message)
}

fun String.e() {
    val message = "${getPrefix()} FATAL $this"
    Log.e(getPrefix(), message)
}

fun Throwable.e() {
    val message = "${getPrefix()} FATAL"
    Log.e(getPrefix(), message, this)
}

private fun getPrefix(stackSize: Int = 6): String {
    val stackTrace = Thread.currentThread().stackTrace
    return if (stackTrace.size >= stackSize) {
        val stackTraceItem = Thread.currentThread().stackTrace[stackSize - 1]
        val lineNumber = stackTraceItem.lineNumber
        val className = stackTraceItem.className
        val methodName = stackTraceItem.methodName
        "[$className:$lineNumber][$methodName()] "
    } else {
        ""
    }
}