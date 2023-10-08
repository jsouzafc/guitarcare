package com.souza.careguitar.utils

import android.app.Application
import com.souza.careguitar.di.appModule
import com.souza.careguitar.di.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            logger(AndroidLogger())
            modules(
                listOf(
                    appModule,
                    navigationModule,
                )
            )
        }
    }
}