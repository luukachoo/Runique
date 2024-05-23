package com.luukachoo.auth.data

import com.luukachoo.auth.domain.AuthRepository
import com.luukachoo.core.data.networking.post
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(email = email, password = password)
        )
    }
}