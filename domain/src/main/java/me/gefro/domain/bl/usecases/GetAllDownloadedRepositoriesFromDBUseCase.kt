package me.gefro.domain.bl.usecases

import kotlinx.coroutines.flow.Flow
import me.gefro.domain.bl.repository.DownloadedFilesRepository
import me.gefro.domain.models.github.downloaded.DownloadedFile

class GetAllDownloadedRepositoriesFromDBUseCase(
    private val downloadedFilesRepository: DownloadedFilesRepository
) {

    suspend fun execute(): Flow<List<DownloadedFile>> = downloadedFilesRepository.getAllDownloadedFilesInfo()

}