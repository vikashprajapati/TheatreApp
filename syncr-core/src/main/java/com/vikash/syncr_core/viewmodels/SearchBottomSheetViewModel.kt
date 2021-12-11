package com.vikash.syncr_core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vikash.syncr_core.data.repository.YoutubeRepository
import kotlinx.coroutines.launch

class SearchBottomSheetViewModel : ViewModel() {
    private val _searchEditText = MutableLiveData<String>()
    private val _invalidInput = MutableLiveData<com.vikash.syncr_core.utils.Event<String>>()


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

    fun getSearchResults() {
        val youtubeRepository = YoutubeRepository()
        viewModelScope.launch(){
            val response = youtubeRepository.search(_searchEditText.value!!)
        }
    }
}