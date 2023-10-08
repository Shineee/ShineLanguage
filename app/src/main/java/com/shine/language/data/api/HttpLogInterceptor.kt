package com.shine.language.data.api

import android.text.TextUtils
import com.shine.language.util.log.L
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class HttpLogInterceptor(private val logEnable: Boolean) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // chain中拿要拦截的信息
        if (!logEnable) return chain.proceed(chain.request())

        // request
        val request = chain.request()
        val method = request.method
        val url = request.url
        L.d(TAG, "Request start")
        L.d(TAG, "$method $url")

        // connection
        val connection = chain.connection()
        val protocol = connection?.protocol()
        protocol?.let { L.d(TAG, "protocol:$it") }

        // headers
        val requestHeaders = request.headers
        requestHeaders.forEach {
            L.d(TAG, "[header]${it.first}:${it.second}")
        }

        // body
        val reqBody = request.body
        reqBody?.contentType()?.let { L.d(TAG, "Content-Type:${it}") }
        reqBody?.contentLength()?.let { L.d(TAG, "Content-Length:${it}") }
        L.d(TAG, "Request $method end")




        L.d(TAG, "Response $method start")
        val startNs = System.nanoTime()
        // response
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            L.d(TAG, "Failed, e:${e.message}")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        L.d(TAG, "code:${response.code} ($tookMs ms)")

        // headers
        val respHeaders = response.headers
        respHeaders.forEach {
            L.d(TAG, "[header]${it.first}:${it.second}")
        }

        // body
        val respBody = response.body
        response.message.let {
            if (!TextUtils.isEmpty(it)) L.d(TAG, "message:${it}")
        }

        val contentLength = respBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"

        if (a(respHeaders)) {
            L.d(TAG, "Response $method end ($bodySize)")
        } else {
            val source = respBody.source()
            source.request(9223372036854775807L)
            var buffer = source.buffer
            var gzippedLength = 0L
            if ("gzip".equals(respHeaders["Content-Encoding"], true)) {
                gzippedLength = buffer.size
                var gzippedResponseBody: GzipSource? = null
                try {
                    gzippedResponseBody = GzipSource(buffer.clone())
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                } finally {
                    gzippedResponseBody?.close()
                }
            }
            val mediaType = respBody.contentType()
            val charset = mediaType?.charset(Charset.forName("UTF-8"))

            if (!a(buffer)) {
                L.d(TAG, "Response $method end")
                return response
            }

            if (contentLength != 0L) {
                charset?.let {
                    L.d(
                        TAG,
                        "msg:${buffer.clone().readString(it)}"
                    )
                }
            }

            L.d(
                TAG, "Response $method end (${buffer.size}-byte"
                        + if (gzippedLength != 0L) ", $gzippedLength--gzipped-byte)" else ")"
            )
        }
        return response
    }

    private fun a(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals(
            "identity",
            true
        ) && !contentEncoding.equals("gzip", true)
    }

    private fun a(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64L) buffer.size else 64L
            buffer.copyTo(prefix, 0L, byteCount)
            var i = 0
            while (i < 16 && !prefix.exhausted()) {
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
                ++i
            }
            true
        } catch (var6: EOFException) {
            false
        }
    }

    companion object {
        private const val TAG = "HttpLogInterceptor"
    }
}