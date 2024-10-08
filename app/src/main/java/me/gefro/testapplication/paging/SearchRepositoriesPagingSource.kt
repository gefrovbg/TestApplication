package me.gefro.testapplication.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.gefro.domain.bl.usecases.GetListRepositoriesBySearchUseCase
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesItemDto

class SearchRepositoriesPagingSource(
    private val getListRepositoriesBySearchUseCase: GetListRepositoriesBySearchUseCase,
    private val search: String
): PagingSource<Int, SearchRepositoriesItemDto>() {
    override fun getRefreshKey(state: PagingState<Int, SearchRepositoriesItemDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchRepositoriesItemDto> {
        return try {
            if (search.isNotBlank()) {
                val nextPage = params.key ?: 1
                val request = getListRepositoriesBySearchUseCase.execute(
                    perPage = 20,
                    page = nextPage,
                    search = search
                )

                val response = request

            if (response != null){
                    LoadResult.Page(
                        data = response,
                        prevKey = if (nextPage == 1) null else nextPage - 1,
                        nextKey = if (response.isEmpty()) null else nextPage + 1
                    )
                }else{
                    LoadResult.Error(Throwable("Empty search"))
                }
            }else{
                LoadResult.Error(Throwable("Empty search"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}