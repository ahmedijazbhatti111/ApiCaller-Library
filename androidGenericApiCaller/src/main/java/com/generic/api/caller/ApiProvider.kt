package com.generic.api.caller

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {
    fun provideApi(
        context: Context,
        baseUrl: String?,
        headerHashMap: HashMap<String, String>
    ): ApiService {
        val gson = GsonBuilder().setLenient().create()

        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val cache = Cache(context.cacheDir, cacheSize)
/*
        val rewriteCacheControlInterceptor = Interceptor { chain ->
            val originalResponse = chain.proceed(chain.request())

            if (context.isNetworkAvailable()) {
                val maxAge = 60 // read from cache for 1 minute
                originalResponse.newBuilder().also { builder ->
                    builder.header("Cache-Control", "public, max-age=$maxAge")
                    headerHashMap.forEach {
                        builder.header(it.key, it.value)
                    }
                }.build()

            } else {
                val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
        }
*/

        val interceptor = Interceptor { chain ->
            chain.proceed(chain.request()).also {
                Header.apply {
                    headers.clear()
                    if (isHeader)
                        it.headers.forEach {
                            headers[it.first] = it.second
                        }
                }
            }.newBuilder().also { builder ->
                headerHashMap.forEach {
                    builder.header(it.key, it.value)
                }
            }.build()
        }
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            level = HttpLoggingInterceptor.Level.BASIC
            level = HttpLoggingInterceptor.Level.HEADERS
        }
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            /*.addNetworkInterceptor(rewriteCacheControlInterceptor)*/
            .addInterceptor(interceptor)
            .addInterceptor(logging)
            .cache(cache)
            .build()

        return if (baseUrl != null)
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
        else
            Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiService::class.java)
    }
}