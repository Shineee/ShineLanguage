package com.shine.language.data.api

import com.shine.language.MainApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object RetrofitClient {

    //    private const val BASE_URL = "http://www.abcd.com/"
    private const val BASE_URL = "https://www.baidu.com/"
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(getOkHttpClient())
        .build()

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()

        val cacheFile = File(MainApplication.instance?.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50)// 50M 的缓存大小

        builder.run {
            cache(cache)
            connectTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)//错误重连
            addInterceptor(HttpLogInterceptor(true))
            addInterceptor(RequestInterceptor())
            getX509TrustManager()?.let { sslSocketFactory(getSSLSocketFactory(), it) }
            // 信任手机所有CA证书
            hostnameVerifier { _, _ -> true }
        }
        return builder.build()
    }

    private fun getSSLSocketFactory(): SSLSocketFactory {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // 信任客户端证书
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                    // 信任服务端证书
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    TODO("Not yet implemented")
                }

                val acceptedIssuers: Array<Any?>?
                    get() = arrayOfNulls(0)
            }
        )

        val sslContext: SSLContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom())
        return sslContext.socketFactory
    }

    private fun getX509TrustManager(): X509TrustManager? {
        var trustManager: X509TrustManager? = null
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            val keystore = KeyStore.getInstance(KeyStore.getDefaultType())
            keystore.load(null)
            trustManagerFactory.init(keystore)
            var trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = trustManagers[0] as X509TrustManager?
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return trustManager
    }

}