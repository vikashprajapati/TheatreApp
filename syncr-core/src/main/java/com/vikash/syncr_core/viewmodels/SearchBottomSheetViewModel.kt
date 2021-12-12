package com.vikash.syncr_core.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import io.ktor.http.cio.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchBottomSheetViewModel : ViewModel() {
    private final val TAG = SearchBottomSheetViewModel::class.java.canonicalName
    private val _searchEditText = MutableLiveData<String>()
    private val _invalidInput = MutableLiveData<com.vikash.syncr_core.utils.Event<String>>()
    private val youtubeRepository = YoutubeRepository(NetworkDataSource(YoutubeApi()))

    var searchEditText : MutableLiveData<String>
        get() = _searchEditText
        set(data) {
            _searchEditText.value = data.value
        }
    val invalidInput : LiveData<com.vikash.syncr_core.utils.Event<String>> get() = _invalidInput

    fun validateInput() {
        if(_searchEditText.value.isNullOrEmpty()){
            _invalidInput.postValue(com.vikash.syncr_core.utils.Event("Search field is empty"))
            return
        }

        getSearchResults()
    }

    private fun getSearchResults() {
        viewModelScope.launch(){
            val response = youtubeRepository.search(_searchEditText.value!!)
            processSearchResponse(response)
        }
    }

    private fun processSearchResponse(response: Result<List<String>>){
        Log.i(TAG, "processSearchResponse: $response")
        /*if(response is ){

        }else{

        }*/
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}