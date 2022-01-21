package com.crystallake.base.net

import android.util.Log
import com.crystallake.base.utils.Util
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.crystallake.base.net.convert.NullConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private lateinit var BASE_URL: String
    private var mInterceptors: List<Interceptor> = arrayListOf()
    private var localServiceMap = mutableMapOf<Class<*>, Any>()

    fun setup(baseUrl: String, interceptors: List<Interceptor>) {
        BASE_URL = baseUrl
        mInterceptors = interceptors
    }

    private val cookiePersistor by lazy {
        SharedPrefsCookiePersistor(Util.getApp())
    }

    private val cookieJar by lazy { PersistentCookieJar(SetCookieCache(), cookiePersistor) }

    private val logger by lazy {
        HttpLoggingInterceptor.Logger {
            Log.i(this::class.simpleName, it)
        }
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor(logger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient by lazy {
        val build = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            .addNetworkInterceptor(loggingInterceptor)
        mInterceptors.forEach {
            build.addInterceptor(it)
        }
        build.build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(NullConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return if (localServiceMap.containsKey(service)) {
            localServiceMap[service] as T
        } else {
            val localService =
                retrofit.create(service) ?: throw RuntimeException("Api service is null!")
            localServiceMap[service] = localService
            localService
        }
    }

    fun clearCookie() = cookieJar.clear()

    fun hasCookie() = cookiePersistor.loadAll().isNotEmpty()
}