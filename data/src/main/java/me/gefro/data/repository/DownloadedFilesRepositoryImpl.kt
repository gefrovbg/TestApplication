package me.gefro.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import me.gefro.data.room.database.DownloadedFilesDatabase
import me.gefro.domain.bl.repository.DownloadedFilesRepository
import me.gefro.domain.models.github.downloaded.DownloadedFile

class DownloadedFilesRepositoryImpl(
    private val context: Context,
): DownloadedFilesRepository {

    private val database by lazy { DownloadedFilesDatabase.getDatabase(context = context) }

    override suspend fun saveInfo(downloadedFile: DownloadedFile) {
        database.downloadedFilesDAO().insert(downloadedFile.convert())
    }

    override suspend fun getAllDownloadedFilesInfo(): Flow<List<DownloadedFile>> = callbackFlow {
        val liveData: LiveData<List<me.gefro.data.room.models.DownloadedFile>> = database.downloadedFilesDAO().getAll()

        val observer: (List<me.gefro.data.room.models.DownloadedFile>?) -> Unit = { downloadedFiles ->
            downloadedFiles?.let {
                trySend(it.map { file -> file.convert() }).isSuccess
            }
        }

        liveData.observeForever(observer)

        awaitClose {
            liveData.removeObserver(observer)
        }
    }

    private fun DownloadedFile.convert(): me.gefro.data.room.models.DownloadedFile{
        return me.gefro.data.room.models.DownloadedFile(
            repo = this.repo,
            owner = this.owner,
            rate = this.rate,
            fileName = this.fileName,
            description = this.description
        )
    }

    private fun me.gefro.data.room.models.DownloadedFile.convert(): DownloadedFile{
        return DownloadedFile(
            repo = this.repo,
            owner = this.owner,
            rate = this.rate,
            fileName = this.fileName,
            description = this.description
        )
    }

}