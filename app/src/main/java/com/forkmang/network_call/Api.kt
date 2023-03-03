package com.forkmang.network_call

import android.annotation.SuppressLint
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object Api {
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null

    @JvmStatic
    val info: APIService
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(WebApi.API_BASE)
                    .client(unsafeOkHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            val api = retrofit!!.create(
                APIService::class.java
            )
            Log.d("Retrofit", "" + api)
            return api
        }

    // Create a trust manager that does not validate certificate chains
    val unsafeOkHttpClient: OkHttpClient

    // Install the all-trusting trust manager

    // Create an ssl socket factory with our all-trusting manager
    .Builder
        get() = try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            builder.addNetworkInterceptor(StethoInterceptor())
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}