package me.gefro.data.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


class KtorApi() {

    @OptIn(ExperimentalSerializationApi::class)
    private val json =  Json {
        ignoreUnknownKeys = true
        useAlternativeNames = false
        prettyPrint = true
        prettyPrintIndent = "   "
    }

    val client: HttpClient = HttpClient {
        defaultRequest {
            url("https://api.github.com")
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(HttpTimeout){
            requestTimeoutMillis = Long.MAX_VALUE
            socketTimeoutMillis = Long.MAX_VALUE
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object: Logger {
                override fun log(message: String) {
                    printTextToLogcat(
                        str = try {
                            val body = getSubstringBetween(
                                input = message,
                                start = "BODY START\n",
                                end = "\nBODY END"
                            )
                            "${message.substringBefore("BODY START")}\nBody:\n${
                                if (!body.isNullOrBlank()) {
                                    try {
                                        json.encodeToString(
                                            json.decodeFromString<JsonElement>(
                                                body
                                            )
                                        )
                                    } catch (e: Exception) {
                                        "Error:\n$e\n\nBut message:\n$message"
                                    }
                                } else {
                                    "Body is empty"
                                }
                            }"
                        }catch (e: Exception){
                            e.message.toString()
                        }
                    )
                }
            }
        }
    }

    private fun printTextToLogcat(
        str: String
    ){
        if (str.length > 2000) {
            Log.d("Ktor Client", str.substring(0, 2000))
            printTextToLogcat(str.substring(2000))
        } else{
            Log.d("Ktor Client", str)
        }
    }

    fun getSubstringBetween(input: String, start: String, end: String): String? {
        val startIndex = input.indexOf(start)
        val endIndex = input.indexOf(end, startIndex + start.length)

        return if (startIndex != -1 && endIndex != -1) {
            input.substring(startIndex + start.length, endIndex)
        } else {
            null
        }
    }
}