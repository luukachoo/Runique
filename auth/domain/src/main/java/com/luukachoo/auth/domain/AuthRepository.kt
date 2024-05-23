package com.luukachoo.auth.domain

import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>
}