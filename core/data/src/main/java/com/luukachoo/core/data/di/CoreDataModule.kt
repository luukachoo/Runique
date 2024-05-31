package com.luukachoo.core.data.di

import com.luukachoo.core.data.auth.EncryptedSessionStorage
import com.luukachoo.core.data.networking.HttpClientFactory
import com.luukachoo.core.domain.SessionStorage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single {
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}