package com.luukachoo.run.network.di

import com.luukachoo.core.domain.run.RemoteRunDataSource
import com.luukachoo.run.network.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
}