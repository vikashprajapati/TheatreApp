package com.vikash.syncr_core.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikash.syncr_core.data.connections.SocketManager
import com.vikash.syncr_core.data.models.response.youtube.searchResults.VideosItem
import com.vikash.syncr_core.data.models.response.youtube.searchResults.YoutubeRefinedSearchResponse
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.events.VideoUrlExtractedEvent
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import com.vikash.syncr_core.utils.Event
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

class SearchFragmentViewModel : ViewModel() {
    private final val TAG = SearchFragmentViewModel::class.java.canonicalName
    private val _searchEditText = MutableLiveData<String>()
    private val _invalidInput = MutableLiveData<Event<String>>()
    private val _searchResults = MutableLiveData<List<VideosItem?>?>()
    private val _searchError = MutableLiveData<Event<String>>()
    private val youtubeRepository = YoutubeRepository(NetworkDataSource(YoutubeApi()))

    val searchResults : LiveData<List<VideosItem?>?> get() = _searchResults
    val searchError : LiveData<Event<String>> get() = _searchError
    val _loading = MutableLiveData<Int>(View.GONE)
    val loading : LiveData<Int> get() = _loading
    var searchEditText : MutableLiveData<String>
        get() = _searchEditText
        set(data) {
            _searchEditText.value = data.value
        }

    init {
        _loading.postValue(View.GONE)
    }

    val invalidInput : LiveData<Event<String>> get() = _invalidInput


    fun validateInput() {
        if(_searchEditText.value.isNullOrEmpty()){
            _invalidInput.postValue(Event("Search field is empty"))
            return
        }

        getSearchResults()
    }

    private fun getSearchResults(){
        _loading.postValue(View.VISIBLE)
        viewModelScope.launch(){
            val response = youtubeRepository.search(_searchEditText.value!!)

            _loading.postValue(View.GONE)
            processSearchResponse(response)
        }
    }

    private fun processSearchResponse(searchResponse: Result<YoutubeRefinedSearchResponse>){
        Log.i(TAG, "processSearchResponse: $searchResponse")
        if(searchResponse.isSuccess){
            _searchResults
                .postValue(searchResponse?.getOrNull()?.items!!)
        }else{
            _searchError.postValue(Event("Unable to retrieve search results"))
        }
    }

    fun sendNewVideoEvent(videoDetails : NewVideoSelected){
        SocketManager.sendNewVideoSelectedEvent(videoDetails)
    }

    fun extractYoutubeUrl(videoId : String, videoTitle : String, context : Context){
        viewModelScope.launch {
            val videoUrl = youtubeRepository.extractVideoUrl(context, videoId)
            if(!videoUrl.isEmpty()){
                withContext(Dispatchers.Main){
                    EventBus.getDefault().post(VideoUrlExtractedEvent(videoUrl, videoTitle))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}