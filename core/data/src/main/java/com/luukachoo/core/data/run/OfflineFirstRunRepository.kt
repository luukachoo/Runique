package com.luukachoo.core.data.run

import com.luukachoo.core.data.networking.get
import com.luukachoo.core.database.dao.RunPendingSyncDao
import com.luukachoo.core.database.mappers.toRunModel
import com.luukachoo.core.domain.AuthInfo
import com.luukachoo.core.domain.SessionStorage
import com.luukachoo.core.domain.run.LocalRunDataSource
import com.luukachoo.core.domain.run.RemoteRunDataSource
import com.luukachoo.core.domain.run.Run
import com.luukachoo.core.domain.run.RunId
import com.luukachoo.core.domain.run.RunRepository
import com.luukachoo.core.domain.util.DataError
import com.luukachoo.core.domain.util.EmptyResult
import com.luukachoo.core.domain.util.Result
import com.luukachoo.core.domain.util.asEmptyDataResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope,
    private val runPendingSyncDao: RunPendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val client: HttpClient
) : RunRepository {
    override fun getRuns(): Flow<List<Run>> = localRunDataSource.getRuns()

    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }

            is Result.Error -> result.asEmptyDataResult()
        }
    }

    override suspend fun upsertRun(run: Run, mapPicture: ByteArray): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        return when (
            val remoteResult = remoteRunDataSource.postRun(runWithId, mapPicture)
        ) {
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteResult.data).asEmptyDataResult()
                }.await()
            }

            is Result.Error -> Result.Success(Unit)
        }
    }

    override suspend fun deleteRun(id: RunId) {
        localRunDataSource.deleteRunById(id = id)

        // if run is created in offline mode and then deleted in offline mode as well,
        // in that case we are not syncing anything
        val isPendingSync = runPendingSyncDao.getRunPendingSyncEntity(id) != null
        if (isPendingSync) {
            runPendingSyncDao.deleteRunPendingSyncEntity(id)
            return
        }
        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(id)
        }.await()
    }

    override suspend fun syncPendingRuns() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.userId ?: return@withContext

            val createdRuns = async { runPendingSyncDao.getAllRunPendingSyncEntities(userId) }
            val deletedRuns = async { runPendingSyncDao.getAllDeletedRunSyncEntities(userId) }
            val createJobs = createdRuns
                .await()
                .map {
                    launch {
                        val run = it.run.toRunModel()
                        when (remoteRunDataSource.postRun(run, it.mapPictureBytes)) {
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteRunPendingSyncEntity(it.runId)
                                }.join()
                            }

                            is Result.Error -> Unit
                        }
                    }
                }
            val deleteJobs = deletedRuns
                .await()
                .map {
                    launch {
                        when (remoteRunDataSource.deleteRun(it.runId)) {
                            is Result.Success -> {
                                applicationScope.launch {
                                    runPendingSyncDao.deleteDeletedRunSyncEntity(it.runId)
                                }.join()
                            }

                            is Result.Error -> Unit
                        }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

    override suspend fun logout(): EmptyResult<DataError.Network> {
        val result = client.get<Unit>(
            route = "/logout"
        ).asEmptyDataResult()

        client.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()

        return result
    }

    override suspend fun deleteAllRuns() = localRunDataSource.deleteAllRuns()
}