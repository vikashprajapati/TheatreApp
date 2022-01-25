package com.vikash.syncr_core.data.repository

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.core.util.forEach
import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.youtube_extractor.VideoMeta
import com.vikash.youtube_extractor.YouTubeExtractor
import com.vikash.youtube_extractor.YtFile

class YoutubeRepository(
    private val networkDataSource: NetworkDataSource
) {

    suspend fun search(searchQuery : String) : Result<YoutubeRefinedSearchResponse> {
        return networkDataSource.getSearchResults(searchQuery)
    }

    suspend fun extractVideoUrl(context : Context, urlToParse : String) : String{
        var videoUrl = ""
        // context passed is used as a weak reference in YouTubeExtractor
        val youtubeExtractor = object : YouTubeExtractor(context) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    ytFiles.forEach{key, ytFile ->
                        if(
                            ytFile.format.ext.equals("mp4")
                            && ytFile.format.audioBitrate > 0
                            && ytFile.format.height > 0
                        ){
                            videoUrl = ytFile.url
                        }
                    }
                }
            }
        }
        youtubeExtractor.extract(urlToParse)
        return videoUrl
    }
}