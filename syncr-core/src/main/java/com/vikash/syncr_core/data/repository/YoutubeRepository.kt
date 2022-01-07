package com.vikash.syncr_core.data.repository

import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import com.vikash.syncr_core.network.NetworkDataSource

class YoutubeRepository(private val networkDataSource: NetworkDataSource) {

    suspend fun search(searchQuery : String) : Result<YoutubeRefinedSearchResponse> {
        return networkDataSource.getSearchResults(searchQuery)
    }
}