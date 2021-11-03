package com.example.theatreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.models.participants.Participant
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData
import com.example.theatreapp.utils.Event

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private var connectionState = MutableLiveData<Event<String>>()
    private val _participants = MutableLiveData<MutableList<ParticipantsItem>>()

    // participants to be changed to livedata of list<participantItem>
    var participants : LiveData<List<ParticipantsItem>> = _participants as LiveData<List<ParticipantsItem>>

    private var participantsObserver = Observer<Event<ParticipantsItem>>{
        val participant = it.getContentIfNotHandledOrReturnNull() ?: return@Observer
        val temporaryParticipantList = _participants.value
        temporaryParticipantList?.add(participant)
        _participants.postValue(SessionData.currentRoom?.participants)
    }

    init {
        _participants.postValue(SessionData.currentRoom?.participants)
        connectionState.postValue(SocketManager.connectionState.value)
        SocketManager.participantJoined.observeForever(participantsObserver)
    }
}