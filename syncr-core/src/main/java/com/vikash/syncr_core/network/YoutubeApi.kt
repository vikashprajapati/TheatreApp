package com.vikash.syncr_core.network

import android.accounts.NetworkErrorException
import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import io.ktor.client.*
import io.ktor.client.request.*

class YoutubeApi {
    val BASE_URL = "https://youtube-v31.p.rapidapi.com"
    val BASE_URL_NEW = "https://youtube-search-results.p.rapidapi.com/youtube-search/"
    private val httpClient : HttpClient get() = ApiWorker().httpClient

    suspend fun searchVideos(searchQuery : String) : Result<YoutubeRefinedSearchResponse>{
        return try{
            if(isNetworkAvailable()){
                val url = BASE_URL_NEW
                val searchResults : YoutubeRefinedSearchResponse = httpClient.get(url){
                    headers {
                        append("x-rapidapi-host", "youtube-search-results.p.rapidapi.com")
                        append("x-rapidapi-key", "60cfeb19c8msh1f36abd0b0581cap1de496jsnbc8185235dd9")
                    }
                    parameter("q", searchQuery)
                }
                Result.success(searchResults)
            }else{
                Result.failure(NetworkErrorException())
            }
        }catch (ex : Exception){
            Result.failure(Exception())
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return true
    }
}