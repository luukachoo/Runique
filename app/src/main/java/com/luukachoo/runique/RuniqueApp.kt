package com.luukachoo.runique

import android.app.Application
import com.luukachoo.auth.data.di.authDataModule
import com.luukachoo.auth.presentation.di.authViewModelModule
import com.luukachoo.core.data.di.coreDataModule
import com.luukachoo.run.presentation.di.runViewModelModule
import com.luukachoo.runique.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@RuniqueApp)
            modules(
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                runViewModelModule
            )
        }
    }
}