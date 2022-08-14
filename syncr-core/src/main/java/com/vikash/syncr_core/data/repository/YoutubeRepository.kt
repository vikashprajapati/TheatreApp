package com.vikash.syncr_core.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.SparseArray
import androidx.core.util.forEach
import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.youtube_extractor.VideoMeta
import com.vikash.youtube_extractor.YouTubeExtractor
import com.vikash.youtube_extractor.YtFile
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class YoutubeRepository(
    private val networkDataSource: NetworkDataSource,
    private val context: Context
) {
    private val TAG = YoutubeRepository::class.simpleName

    suspend fun search(searchQuery : String) : Result<YoutubeRefinedSearchResponse> {
        return networkDataSource.getSearchResults(searchQuery)
    }

    @SuppressLint("StaticFieldLeak")
    suspend fun extractVideoUrl(urlToParse : String) : String = suspendCoroutine{
        // context passed is used as a weak reference in YouTubeExtractor
        val youtubeExtractor = object : YouTubeExtractor(context) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    var videoUrl = ""
                    ytFiles.forEach{key, ytFile ->
                        Log.i(TAG, "onExtractionComplete: $key \n $ytFile \n\n")
                        if(
                            ytFile.format.ext.equals("mp4")
                            && ytFile.format.audioBitrate > 0
                            && ytFile.format.height > 0
                        ){
                            videoUrl = ytFile.url
                        }
                    }

                    it.resume(videoUrl)

                    /*if(videoUrl.isNullOrEmpty()){
                        requireActivity().runOnUiThread {
                            shortToast("Can't play video")
                        }
                    }else{
                        exoplayer.prepare(
                            ProgressiveMediaSource
                                .Factory(dataSourceFactory)
                                .createMediaSource(Uri.parse(getProxyUrl(videoUrl)))
                        )
                    }*/
                }
            }
        }
        youtubeExtractor.extract(urlToParse)
    }
}