package me.gefro.testapplication.ui.screens.main

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import me.gefro.domain.bl.usecases.DownloadRepositoryArchiveUseCase
import me.gefro.domain.bl.usecases.GetListRepositoriesBySearchUseCase
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.testapplication.paging.SearchRepositoriesPagingData
import me.gefro.testapplication.paging.SearchRepositoriesPagingSource
import me.gefro.testapplication.services.DownloadService

class MainScreenViewModel(
    private val searchRepositoriesPagingData: SearchRepositoriesPagingData,
    private val downloadRepositoryArchiveUseCase: DownloadRepositoryArchiveUseCase,
    private val downloadService: DownloadService
): ViewModel() {

    val lazyListState = MutableStateFlow(LazyListState()).asStateFlow()

    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    val search: StateFlow<String> = _search

    fun setSearch(value: String){
        if (value.isNotBlank()){
            setRefreshing(true)
        }
        _search.value = value
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchedListRepositories: Flow<PagingData<RepositoriesItemDto>> = search.debounce(1000).flatMapLatest{ lastSearch ->
        if (lastSearch.isNotBlank()) {
            searchRepositoriesPagingData.getFlowData(lastSearch)
        }else{
            setRefreshing(false)
            flowOf(PagingData.empty())
        }
    }.cachedIn(viewModelScope)


    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing

    fun setRefreshing(value: Boolean) {
        _refreshing.value = value
    }

    fun download(item: RepositoriesItemDto){
        downloadService.setDownloadFile(
            Pair(
                item,
                Pair(
                    0,
                    -1
                )
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            downloadRepositoryArchiveUseCase.execute(
                item = item,
                onDownload = { done, size ->
                    Log.d("download", "$done/$size")
                    downloadService.setDownloadFile(
                        Pair(
                            item,
                            Pair(
                                done,
                                size
                            )
                        )
                    )
                },
                isDownloaded = {
                    downloadService.setDownloadFile(null)
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        setSearch("")
    }

}