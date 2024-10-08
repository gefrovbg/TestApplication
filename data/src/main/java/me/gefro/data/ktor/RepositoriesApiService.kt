package me.gefro.data.ktor

import android.os.Environment
import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessage
import io.ktor.http.contentType
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.cancel
import me.gefro.data.tools.getFileExtensionFromMimeType
import me.gefro.data.tools.pathUrl
import me.gefro.domain.bl.repository.DownloadedFilesRepository
import me.gefro.domain.models.Resource
import me.gefro.domain.models.github.RepositoriesItemDto
import me.gefro.domain.models.github.downloaded.DownloadedFile
import me.gefro.domain.models.github.search.SearchRepositoriesItemDto
import me.gefro.domain.models.github.search.SearchRepositoriesListDto
import java.io.File
import java.io.FileOutputStream

class RepositoriesApiService(
    private val ktorApi: KtorApi,
    private val downloadedFilesRepository: DownloadedFilesRepository
) {

    suspend fun getListRepositoriesBySearch(
        search: String,
        perPage: Int,
        page: Int
    ): List<SearchRepositoriesItemDto>? {
        return try {
            val param = mutableListOf(
                Pair("per_page","$perPage"),
                Pair("page","$page")
            )
            ktorApi.client.get {
                pathUrl(
                    path = "/users/$search/repos",
                    param = param
                )
                contentType(ContentType.Application.Json)
            }.body<List<SearchRepositoriesItemDto>>()
        } catch (e: Exception){
            null
        } catch (e: Throwable){
            null
        }
    }

    suspend fun downloadRepositoryArchive(
        item: RepositoriesItemDto,
        onDownload:(done: Long, size: Long) -> Unit,
        isDownloaded:(value: Boolean) -> Unit
    ){
        val repo = item.name
        val owner = item.owner?.login
        if (repo != null && owner != null) {
            try {
                ktorApi.client.prepareGet {
                    pathUrl(path = "repos/$owner/$repo/zipball")
                    timeout {
                        requestTimeoutMillis = Long.MAX_VALUE
                    }
                }.execute { httpResponse ->
                    val mimeType = httpResponse.contentType().toString()
                    val contentDisposition = httpResponse.headers[HttpHeaders.ContentDisposition]
                    val contentLength = httpResponse.headers["Content-Length"]?.toLongOrNull() ?: -1L
                    val fileName = extractFileName(contentDisposition)
                        ?: "downloaded_file${getFileExtensionFromMimeType(mimeType)}"
                    val channel: ByteReadChannel = httpResponse.body()
                    val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File(downloadsFolder, fileName)

                    var totalBytesRead = 0L

                    FileOutputStream(file).use { output ->
                        while (!channel.isClosedForRead) {
                            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                            while (!packet.isEmpty) {
                                val bytes = packet.readBytes()
                                output.write(bytes)
                                totalBytesRead += bytes.size
                                onDownload(totalBytesRead, contentLength)
                            }
                        }
                    }
                    isDownloaded(true)
                    httpResponse.cancel()
                    downloadedFilesRepository.saveInfo(
                        DownloadedFile(
                            fileName = fileName,
                            repo = repo,
                            owner = owner,
                            rate = item.watchers ?: 0,
                            description = item.description ?: ""
                        )
                    )
                }
            }catch (e: Exception){
                isDownloaded(false)
            }
        }else{

        }
    }

    private fun extractFileName(contentDisposition: String?): String? {
        return contentDisposition?.let {
            val dispositionParts = it.split(";")
            for (part in dispositionParts) {
                val trimmedPart = part.trim()
                if (trimmedPart.startsWith("filename=", true)) {
                    return trimmedPart.substringAfter('=').trim('"')
                }
            }
            null
        }
    }

}
