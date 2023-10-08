package com.shine.language.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.shine.language.util.log.i

object TimerUtil {
    private const val ACTION_DEVICE = "com.htd.dc.action.DEVICE_PERIOD_TIME"
    private const val PERIOD_ONE_SECOND_TIME = 5 * 1000L
    private const val PERIOD_ONE_HOUR_TIME = 1 * 60 * 60 * 1000L
    fun startTimer(context: Context?) {
        "startTimer()".i()
        val firstTime = System.currentTimeMillis()
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent()
        intent.`package` = context?.packageName
        intent.action = ACTION_DEVICE
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
//        val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
        //先取消定时任务
//        alarmManager.cancel(pendingIntent)
        //设置新的定时任务
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, firstTime + PERIOD_ONE_SECOND_TIME, pendingIntent)
    }
}