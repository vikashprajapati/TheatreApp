package com.vikash.syncr_core.network

import android.accounts.NetworkErrorException
import com.vikash.syncr_core.data.models.response.youtube.searchResults.SearchResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.response.*
import io.ktor.http.cio.*

class YoutubeApi {
    val BASE_URL = "https://youtube-v31.p.rapidapi.com"
    private val httpClient : HttpClient get() = ApiWorker().httpClient

    suspend fun searchVideo(searchQuery : String) : Result<SearchResponse>{
        return try{
            if(isNetworkAvailable()){
                val url = BASE_URL + "/search"
                val searchResults : SearchResponse = httpClient.get(url){
                    headers {
                        append("x-rapidapi-host", "youtube-v31.p.rapidapi.com")
                        append("x-rapidapi-key", "8ed9b5613amshe1b718e52a08c07p170155jsnd3b6c5af6a16")
                    }
                    parameter("q", searchQuery)
                    parameter("maxResults", 50)
                    parameter("order", "date")
                    parameter("part", "snippet,id")
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