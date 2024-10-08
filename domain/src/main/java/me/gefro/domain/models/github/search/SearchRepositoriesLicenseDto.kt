package me.gefro.domain.models.github.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchRepositoriesLicenseDto(
    val html_url: String? = null,
    val key: String? = null,
    val name: String? = null,
    val node_id: String? = null,
    val spdx_id: String? = null,
    val url: String? = null,
)