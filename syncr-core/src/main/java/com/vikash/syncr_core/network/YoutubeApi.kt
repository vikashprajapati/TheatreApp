package com.vikash.syncr_core.network

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*

class YoutubeApi {
    val BASE_URL = ""
    private val httpClient : HttpClient get() = ApiWorker().httpClient

    suspend fun searchVideo(searchQuery : String) : String{
//        val response = httpClient.get(BASE_URL + "/$searchQuery")
        return ""
    }
}