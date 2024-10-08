package me.gefro.domain.models


sealed class Resource<out T> {
    data object Init : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data object Loading : Resource<Nothing>()
    data class Error(val message: String, val code: Int) : Resource<Nothing>()
}