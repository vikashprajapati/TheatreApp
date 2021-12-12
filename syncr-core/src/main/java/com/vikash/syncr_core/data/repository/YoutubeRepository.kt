package com.vikash.syncr_core.data.repository

import android.accounts.NetworkErrorException
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import kotlin.Exception

class YoutubeRepository(private val networkDataSource: NetworkDataSource) {

    suspend fun search(searchQuery : String) : Result<List<String>> {
        return networkDataSource.getSearchResults(searchQuery)
    }
}