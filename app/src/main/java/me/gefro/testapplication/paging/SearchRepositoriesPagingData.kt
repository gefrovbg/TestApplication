package me.gefro.testapplication.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.gefro.domain.bl.usecases.GetListRepositoriesBySearchUseCase
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesItemDto

class SearchRepositoriesPagingData(
    private val getListRepositoriesBySearchUseCase: GetListRepositoriesBySearchUseCase
) {

    fun getFlowData(search: String): Flow<PagingData<SearchRepositoriesItemDto>>{
        return Pager(PagingConfig(pageSize = 20)) {
            SearchRepositoriesPagingSource(
                getListRepositoriesBySearchUseCase = getListRepositoriesBySearchUseCase,
                search = search
            )
        }.flow
    }

}