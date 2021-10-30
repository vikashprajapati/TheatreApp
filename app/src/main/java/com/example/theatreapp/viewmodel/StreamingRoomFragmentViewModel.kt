package com.example.theatreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatreapp.models.participants.Participant

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private val connectionState = MutableLiveData<String>()
    private val participants = MutableLiveData<List<Participant>>()
    var participantsList : MutableLiveData<List<Participant>> = participants

    init {
        participants.value = emptyList()
    }
}