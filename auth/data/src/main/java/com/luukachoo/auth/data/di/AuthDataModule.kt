package com.luukachoo.auth.data.di

import com.luukachoo.auth.data.AuthRepositoryImpl
import com.luukachoo.auth.data.EmailPatternValidator
import com.luukachoo.auth.domain.AuthRepository
import com.luukachoo.auth.domain.PatternValidator
import com.luukachoo.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {
    single<PatternValidator> {
        EmailPatternValidator
    }

    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}