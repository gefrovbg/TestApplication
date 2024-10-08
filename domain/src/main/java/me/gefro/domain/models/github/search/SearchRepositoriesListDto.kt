package me.gefro.domain.models.github.search

import kotlinx.serialization.Serializable
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.RepositoryOwnerItemDto

@Serializable
data class SearchRepositoriesListDto(
    val incomplete_results: Boolean? = null,
    val items: List<SearchRepositoriesItemDto>? = null,
    val total_count: Int? = null,
){
    fun convert(): List<RepositoriesItemDto>{
        val list = mutableListOf<RepositoriesItemDto>()

        items?.forEach { item ->
            list.add(
                RepositoriesItemDto(
                    archive_url = item.archive_url,
                    archived = item.archived,
                    created_at = item.created_at,
                    description = item.description,
                    disabled = item.disabled,
                    downloads_url = item.downloads_url,
                    full_name = item.full_name,
                    id = item.id,
                    language = item.language,
                    languages_url = item.languages_url,
                    name = item.name,
                    node_id = item.node_id,
                    owner = item.owner?.let { ownerItem ->
                        RepositoryOwnerItemDto(
                            avatar_url = ownerItem.avatar_url,
                            id = ownerItem.id,
                            login = ownerItem.login,
                            repos_url = ownerItem.repos_url,
                            url = ownerItem.url
                        )
                    },
                    `private` = item.`private`,
                    size = item.size,
                    tags_url = item.tags_url,
                    updated_at = item.updated_at,
                    url = item.url,
                    visibility = item.visibility,
                    watchers = item.watchers,
                    watchers_count = item.watchers_count,
                    html_url = item.html_url
                )
            )
        }

        return list
    }
}