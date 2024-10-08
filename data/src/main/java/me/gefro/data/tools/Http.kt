package me.gefro.data.tools

import android.webkit.MimeTypeMap
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.delay
import me.gefro.domain.models.Resource

fun HttpRequestBuilder.pathUrl(path: String, param: List<Pair<String, String>>? = null) {
    url {
        appendPathSegments(path)
        if (!param.isNullOrEmpty()){
            param.forEach {
                parameter(it.first, it.second)
            }
        }
    }
}

suspend inline fun <reified T> checkResponse(response: Resource<HttpResponse>): Resource<T> {
    return when(response){
        is Resource.Init -> {
            Resource.Init
        }
        is Resource.Loading -> {
            Resource.Loading
        }
        is Resource.Error -> {
            Resource.Error(response.message, response.code)
        }
        is Resource.Success -> {
            when (val code = response.data.status.value) {
                in 100..399 -> {
                    try {
                        Resource.Success(response.data.body())
                    }catch (e: Exception){
                        Resource.Error(e.message.toString(), 0)
                    }catch (e: Throwable){
                        Resource.Error(e.message.toString(), 1)
                    }
                }
                in 500..599 -> {
                    Resource.Error("Server wrong!", code)
                }
                else -> {
                    try {
                        Resource.Error("Something wrong", code)
                    }catch (e: Exception){
                        Resource.Error(e.message.toString(), 0)
                    }catch (e: Throwable){
                        Resource.Error(e.message.toString(), 1)
                    }

                }
            }
        }
    }
}

fun getFileExtensionFromMimeType(mimeType: String): String? {
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
}