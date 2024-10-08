package me.gefro.testapplication.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.gefro.domain.models.github.RepositoriesItemDto

class DownloadService(

) {

    private val _downloadingFile: MutableStateFlow<Pair<RepositoriesItemDto, Pair<Long, Long>>?> = MutableStateFlow(null)
    val downloadingFile: StateFlow<Pair<RepositoriesItemDto, Pair<Long, Long>>?> = _downloadingFile

    fun setDownloadFile(value: Pair<RepositoriesItemDto, Pair<Long, Long>>?){
        _downloadingFile.value = value
    }

}