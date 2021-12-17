package com.vikash.syncr_core.viewmodels

import android.util.Log
import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikash.syncr_core.data.models.response.youtube.searchResults.ItemsItem
import com.vikash.syncr_core.data.models.response.youtube.searchResults.SearchResponse
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import com.vikash.syncr_core.utils.Event
import io.ktor.http.cio.*
import kotlinx.coroutines.*

class SearchBottomSheetViewModel : ViewModel() {
    private final val TAG = SearchBottomSheetViewModel::class.java.canonicalName
    private val _searchEditText = MutableLiveData<String>()
    private val _invalidInput = MutableLiveData<Event<String>>()
    private val _searchResults = MutableLiveData<List<ItemsItem?>?>()
    private val _searchError = MutableLiveData<Event<String>>()
    private val youtubeRepository = YoutubeRepository(NetworkDataSource(YoutubeApi()))

    val searchResults : LiveData<List<ItemsItem?>?> get() = _searchResults
    val searchError : LiveData<Event<String>> get() = _searchError
    val loading = MutableLiveData<Int>()
    var searchEditText : MutableLiveData<String>
        get() = _searchEditText
        set(data) {
            _searchEditText.value = data.value
        }

    init {
        loading.postValue(View.GONE)
    }

    val invalidInput : LiveData<Event<String>> get() = _invalidInput

    fun validateInput() {
        if(_searchEditText.value.isNullOrEmpty()){
            _invalidInput.postValue(Event("Search field is empty"))
            return
        }

        getSearchResults()
    }

    private fun getSearchResults() {
        loading.postValue(View.VISIBLE)
        viewModelScope.launch(){
            val response = youtubeRepository.search(_searchEditText.value!!)

            loading.postValue(View.GONE)
            processSearchResponse(response)
        }
    }

    private fun processSearchResponse(searchResponse: Result<SearchResponse>){
        Log.i(TAG, "processSearchResponse: $searchResponse")
        if(searchResponse.isSuccess){
            _searchResults.postValue(searchResponse.getOrNull()?.items)
        }else{
            _searchError.postValue(Event("Unable to retrieve search results"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}