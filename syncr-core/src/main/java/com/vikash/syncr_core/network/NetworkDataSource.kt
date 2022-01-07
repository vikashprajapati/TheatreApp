package com.vikash.syncr_core.network

import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse

class NetworkDataSource(private val youtubeApi: YoutubeApi) {
    suspend fun getSearchResults(query : String) : Result<YoutubeRefinedSearchResponse>{
        return youtubeApi.searchVideos(query)
    }
}