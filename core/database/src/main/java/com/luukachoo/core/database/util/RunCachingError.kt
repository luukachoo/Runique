package com.luukachoo.core.database.util

import android.database.sqlite.SQLiteFullException
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.Result

inline fun <T> runCachingError(
    operation: () -> T
): Result<T, DataError.Local> {
    return try {
        val result = operation()
        Result.Success(result)
    } catch (e: SQLiteFullException) {
        Result.Error(DataError.Local.DISK_FULL)
    }
}