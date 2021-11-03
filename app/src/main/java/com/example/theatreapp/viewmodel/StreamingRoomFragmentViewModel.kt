package com.example.theatreapp.viewmodel

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
    private val participants = MutableLiveData<MutableList<ParticipantsItem>>()
    var participantsList : MutableLiveData<MutableList<ParticipantsItem>> = participants

    private var participantsObserver = Observer<Event<ParticipantsItem>>{
        val participant = it.getContentIfNotHandledOrReturnNull() ?: return@Observer
        val temporaryParticipantList = participants.value
        temporaryParticipantList?.add(participant)
        participants.postValue(temporaryParticipantList)
    }

    init {
        participants.value = SessionData.currentRoom?.participants
        connectionState.postValue(SocketManager.connectionState.value)
        SocketManager.participantJoined.observeForever(participantsObserver)
    }
}