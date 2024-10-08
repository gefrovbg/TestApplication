package me.gefro.domain.bl.repository

import kotlinx.coroutines.flow.Flow
import me.gefro.domain.models.github.downloaded.DownloadedFile

interface DownloadedFilesRepository {

    suspend fun saveInfo(downloadedFile: DownloadedFile)

    suspend fun getAllDownloadedFilesInfo(): Flow<List<DownloadedFile>>

}