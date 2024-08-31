package com.luukachoo.runique

import android.app.Application
import com.luukachoo.auth.data.di.authDataModule
import com.luukachoo.auth.presentation.di.authViewModelModule
import com.luukachoo.core.data.di.coreDataModule
import com.luukachoo.core.database.di.databaseModule
import com.luukachoo.run.location.di.locationModule
import com.luukachoo.run.presentation.di.runPresentationModule
import com.luukachoo.runique.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RuniqueApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

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
                runPresentationModule,
                locationModule,
                databaseModule
            )
        }
    }
}