package com.luukachoo.core.data.auth

import com.luukachoo.core.domain.AuthInfo

fun AuthInfo.toAuthInfoSerializable() = AuthInfoSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)

fun AuthInfoSerializable.toAuthInfo() = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    userId = userId
)