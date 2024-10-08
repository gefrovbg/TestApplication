package me.gefro.domain.bl.repository

import kotlinx.coroutines.flow.Flow
import me.gefro.domain.models.Resource
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesListDto

interface RepositoriesRepository {

    suspend fun getListBySearch(search: String, perPage: Int, page: Int): SearchRepositoriesListDto?

    suspend fun downloadRepositoryArchive(
        item: RepositoriesItemDto,
        onDownload:(done: Long, size: Long) -> Unit,
        isDownloaded:(value: Boolean) -> Unit
    )

}