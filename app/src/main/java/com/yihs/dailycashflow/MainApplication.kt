package com.yihs.dailycashflow

import android.app.Application
import com.yihs.dailycashflow.di.appModule
import com.yihs.dailycashflow.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(listOf(networkModule, appModule))
        }


    }
}