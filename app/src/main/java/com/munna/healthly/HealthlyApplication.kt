package com.munna.healthly

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.munna.healthly.di.appModule

class HealthlyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HealthlyApplication)
            modules(appModule)
        }
    }
}
