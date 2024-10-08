package me.gefro.domain.bl.usecases

import kotlinx.coroutines.flow.Flow
import me.gefro.domain.bl.repository.RepositoriesRepository
import me.gefro.domain.models.Resource
import me.gefro.domain.models.github.search.SearchRepositoriesListDto

class GetListRepositoriesBySearchUseCase(
    private val repositoriesRepository: RepositoriesRepository
) {

    suspend fun execute(search: String, perPage: Int, page: Int): SearchRepositoriesListDto? = repositoriesRepository.getListBySearch(search = search, perPage = perPage, page = page)

}