package me.gefro.data.repository

import me.gefro.data.ktor.RepositoriesApiService
import me.gefro.domain.bl.repository.RepositoriesRepository
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesListDto

class RepositoriesRepositoryImpl(
    private val apiService: RepositoriesApiService
): RepositoriesRepository {

    override suspend fun getListBySearch(
        search: String,
        perPage: Int,
        page: Int
    ): List<SearchRepositoriesItemDto>? {

        return apiService.getListRepositoriesBySearch(
            search = search,
            perPage = perPage,
            page = page
        )
    }

    override suspend fun downloadRepositoryArchive(
        item: RepositoriesItemDto,
        onDownload:(done: Long, size: Long) -> Unit,
        isDownloaded:(value: Boolean) -> Unit
    ) {
        apiService.downloadRepositoryArchive(item = item, onDownload = onDownload, isDownloaded = isDownloaded)
    }

}