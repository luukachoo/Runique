package com.luukachoo.auth.data

import com.luukachoo.auth.domain.AuthRepository
import com.luukachoo.core.data.networking.post
import com.luukachoo.core.domain.AuthInfo
import com.luukachoo.core.domain.SessionStorage
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult
import com.luukachoo.core.domain.util.Result
import com.luukachoo.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun register(email: String, password: String): EmptyResult<DataError.Network> {
        return httpClient.post<RegisterRequest, Unit>(
            route = "/register",
            body = RegisterRequest(email = email, password = password)
        )
    }

    override suspend fun login(email: String, password: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<LoginRequest, LoginResponse>(
            route = "/login",
            body = LoginRequest(email, password)
        )
        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken,
                    userId = result.data.userId
                )
            )
        }
        return result.asEmptyDataResult()
    }
}