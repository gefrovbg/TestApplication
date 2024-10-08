package me.gefro.domain.bl.usecases

import me.gefro.domain.bl.repository.RepositoriesRepository
import me.gefro.domain.models.github.RepositoriesItemDto

class DownloadRepositoryArchiveUseCase(
    private val repositoriesRepository: RepositoriesRepository
) {

    suspend fun execute(
        item: RepositoriesItemDto,
        onDownload:(done: Long, size: Long) -> Unit,
        isDownloaded:(value: Boolean) -> Unit
    ) {
        repositoriesRepository.downloadRepositoryArchive(item = item, onDownload = onDownload, isDownloaded = isDownloaded)
    }

}