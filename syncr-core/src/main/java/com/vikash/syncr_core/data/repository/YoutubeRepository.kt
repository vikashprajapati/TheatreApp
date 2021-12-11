package com.vikash.syncr_core.data.repository

import com.vikash.syncr_core.network.YoutubeApi
import java.lang.Exception

class YoutubeRepository {
    val youtubeApi = YoutubeApi()

    suspend fun search(searchQuery : String) : String{
        try {
            return youtubeApi.searchVideo(searchQuery)
        }catch (exception : Exception){

        }

        return ""
    }
}