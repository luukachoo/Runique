package com.luukachoo.run.location.di

import com.luukachoo.run.domain.LocationObserver
import com.luukachoo.run.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}