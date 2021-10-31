package com.example.theatreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.models.participants.Participant
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData
import com.example.theatreapp.utils.Event

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private var connectionState = MutableLiveData<Event<String>>()
    private val participants = MutableLiveData<List<ParticipantsItem>>()
    var participantsList : MutableLiveData<List<ParticipantsItem>> = participants

    init {
        participants.value = SessionData.participants
        connectionState.postValue(SocketManager.connectionState.value)
    }
}