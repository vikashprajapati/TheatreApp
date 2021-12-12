package com.vikash.syncr_core.network

import io.ktor.http.cio.*

class NetworkDataSource(private val youtubeApi: YoutubeApi) {

    suspend fun getSearchResults(query : String) : Result<List<String>>{
        return youtubeApi.searchVideo(query)
    }
}