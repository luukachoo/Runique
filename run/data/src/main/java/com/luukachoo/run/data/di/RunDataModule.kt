package com.luukachoo.run.data.di

import com.luukachoo.run.data.CreateRunWorker
import com.luukachoo.run.data.DeleteRunWorker
import com.luukachoo.run.data.FetchRunsWorker
import com.luukachoo.run.data.SyncRunWorkerScheduler
import com.luukachoo.run.domain.SyncRunScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::CreateRunWorker)
    workerOf(::FetchRunsWorker)
    workerOf(::DeleteRunWorker)

    singleOf(::SyncRunWorkerScheduler).bind<SyncRunScheduler>()
}