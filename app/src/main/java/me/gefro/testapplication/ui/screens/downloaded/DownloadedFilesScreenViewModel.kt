package me.gefro.testapplication.ui.screens.downloaded

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import me.gefro.domain.bl.usecases.GetAllDownloadedRepositoriesFromDBUseCase
import me.gefro.domain.models.github.downloaded.DownloadedFile

class DownloadedFilesScreenViewModel(
    private val getAllDownloadedRepositoriesFromDBUseCase: GetAllDownloadedRepositoriesFromDBUseCase
): ViewModel() {

    private val _listFromDb: MutableStateFlow<List<DownloadedFile>> = MutableStateFlow(emptyList())
    val listFromDb: StateFlow<List<DownloadedFile>> = _listFromDb

    suspend fun loadData(){
        withContext(Dispatchers.Main) {
            getAllDownloadedRepositoriesFromDBUseCase.execute().collect { list ->
                _listFromDb.value = list.reversed()
            }
        }
    }

}