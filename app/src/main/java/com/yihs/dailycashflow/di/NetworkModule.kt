package com.yihs.dailycashflow.di

import com.yihs.dailycashflow.BuildConfig
import com.yihs.dailycashflow.data.preferences.UserPreference
import com.yihs.dailycashflow.data.remote.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single { UserPreference(get()) }

    single {
        HttpLoggingInterceptor().apply {
            level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    single<Interceptor> {
        Interceptor{
            val originalRequest = it.request()
            val session = runBlocking { get<UserPreference>().getSession().first() }
            val newRequest = originalRequest.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            if(session.token != "" || session.token.isNotEmpty()){
                newRequest.addHeader("Authorization", "Bearer ${session.token}")
            }

            it.proceed(newRequest.build())
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<Interceptor>())
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    single { GsonConverterFactory.create() }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }





}