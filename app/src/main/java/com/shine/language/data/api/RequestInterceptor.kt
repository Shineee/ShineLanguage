package com.shine.language.data.api

import android.os.Build
import com.shine.language.util.log.L
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.*

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // request
        val original = chain.request()

        // headers
        val formatter = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA)
        val formattedDate = formatter.format(Date())
        val request = original.newBuilder()
//            .header("d_id", getCustomProp("ro.boot.serialno"))
            .header("d_id", Build.ID)
            .header("d_type", Build.BRAND)
            .header("date", formattedDate)
            .header("token", "80f6786A")
            .method(original.method, original.body)
            .build()
        request.headers.forEach {
            L.d(TAG, "[header]${it.first}:${it.second}")
        }
        return chain.proceed(request)
    }

    companion object {
        private const val TAG = "HttpCommonInterceptor"
    }
}