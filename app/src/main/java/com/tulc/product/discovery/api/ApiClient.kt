package com.tulc.product.discovery.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

import com.tulc.product.discovery.BuildConfig


object ApiClient {

    private var instance: ApiInterface? = null

    fun getInstance(): ApiInterface = instance ?: synchronized(this) {
        val logging = HttpLoggingInterceptor()
        logging.apply {
            this.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val client = OkHttpClient.Builder()
            .followRedirects(true)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val requestBuilder =
                    chain.request().newBuilder().url(chain.request().url.toString())

                val request = requestBuilder.build()
                val response = chain.proceed(request)
                response
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_SERVER_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        instance ?: retrofit.create(ApiInterface::class.java)
    }
}