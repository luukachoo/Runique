package com.luukachoo.run.network

import com.luukachoo.core.data.networking.delete
import com.luukachoo.core.data.networking.get
import com.luukachoo.core.data.networking.safeCall
import com.luukachoo.core.domain.run.RemoteRunDataSource
import com.luukachoo.core.domain.run.Run
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult
import com.luukachoo.core.domain.util.Result
import com.luukachoo.core.domain.util.map
import com.luukachoo.run.network.mappers.toCreateRunRequest
import com.luukachoo.run.network.mappers.toRunModel
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRemoteRunDataSource(
    private val httpClient: HttpClient,
) : RemoteRunDataSource {
    override suspend fun getRuns(): Result<List<Run>, DataError.Network> {
        return httpClient.get<List<RunDto>>(
            route = "/runs"
        ).map { runDtos ->
            runDtos.map { it.toRunModel() }
        }
    }

    override suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network> {
        val createRunRequestJson = Json.encodeToString(run.toCreateRunRequest())
        val result = safeCall<RunDto> {
            httpClient.submitFormWithBinaryData(
                url = "/runs",
                formData = formData {
                    append("RUN_DATA", createRunRequestJson, Headers.build {
                        append(HttpHeaders.ContentType, "text/plain")
                        append(HttpHeaders.ContentDisposition, "form-data; name=\"RUN_DATA\"")
                    })
                    append("MAP_PICTURE", mapPicture, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=mappicture.jpg")
                    })
                }
            ) {
                method = HttpMethod.Post
            }
        }
        return result.map { it.toRunModel() }
    }

    override suspend fun deleteRun(id: String): EmptyResult<DataError.Network> {
        return httpClient.delete(
            route = "/run",
            queryParameters = mapOf("id" to id)
        )
    }
}