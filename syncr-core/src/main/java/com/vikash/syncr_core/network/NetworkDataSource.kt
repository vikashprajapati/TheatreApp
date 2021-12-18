package com.vikash.syncr_core.network

import com.vikash.syncr_core.data.models.response.youtube.searchResults.SearchResponse
import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import io.ktor.http.cio.*

class NetworkDataSource(private val youtubeApi: YoutubeApi) {

    suspend fun getSearchResults(query : String) : Result<SearchResponse>{
        return youtubeApi.searchVideo(query)
    }

    suspend fun getRefinedSearchResults(query : String) : Result<YoutubeRefinedSearchResponse>{
        return youtubeApi.searchRefinedResults(query)
    }
}