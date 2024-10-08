package me.gefro.domain.models.github.downloaded

import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.RepositoryOwnerItemDto

data class DownloadedFile(
    val repo: String,
    val owner: String,
    val rate: Int,
    val fileName: String,
    val description: String
){
    fun convertToRepo(): RepositoriesItemDto{
        return RepositoriesItemDto(
            description = this.description,
            owner = RepositoryOwnerItemDto(login = this.owner),
            watchers = this.rate,
            name = this.repo
        )
    }
}