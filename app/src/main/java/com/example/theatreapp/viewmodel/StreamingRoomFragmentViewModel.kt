package com.example.theatreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatreapp.models.participants.Participant
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private val connectionState = MutableLiveData<String>()
    private val participants = MutableLiveData<List<ParticipantsItem>>()
    var participantsList : MutableLiveData<List<ParticipantsItem>> = participants

    init {
        participants.value = SessionData.participants
    }
}