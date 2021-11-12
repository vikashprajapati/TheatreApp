package com.example.theatreapp.ui.fragment.streaming

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.data.connections.SocketManager
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.data.SessionData
import com.example.theatreapp.utils.Event

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private var _connectionState = MutableLiveData<Event<String>>()
    private val _participants = MutableLiveData<MutableList<ParticipantsItem>>()
    private val _videoPlayback = MutableLiveData<Event<String>>()
    private val _videoChanged = MutableLiveData<Event<String>>()
    private val _videoSynced = MutableLiveData<Event<String>>()
    private val _participantLeft = MutableLiveData<Event<ParticipantsItem>>()

    // participants to be changed to livedata of list<participantItem>
    var participants : LiveData<List<ParticipantsItem>> = _participants as LiveData<List<ParticipantsItem>>
    var videoPlayback : LiveData<Event<String>> = _videoPlayback
    var videoChanged : LiveData<Event<String>> = _videoChanged
    var videoSynced : LiveData<Event<String>> = _videoSynced
    var connectionState : LiveData<Event<String>> = _connectionState
    var participantLeft : LiveData<Event<ParticipantsItem>> = _participantLeft

    private var participantJoinedObserver = Observer<Event<ParticipantsItem>>{
        val participant = it.getContentIfNotHandledOrReturnNull() ?: return@Observer
        // participant added previously in SessionData
        _participants.postValue(SessionData.currentRoom?.participants)
    }

    private var videoPlaybackObserver = Observer<Event<String>>{
        _videoPlayback.postValue(it)
    }

    private var videoChangedObserver = Observer<Event<String>>{
        _videoChanged.postValue(it)
    }

    private var videoSyncedObserver = Observer<Event<String>>{
        _videoSynced.postValue(it)
    }

    private var participantLeftObserver = Observer<Event<ParticipantsItem>>{
        _participantLeft.postValue(it)
        _participants.postValue(SessionData.currentRoom?.participants)
    }

    private var connectivityObserver = Observer<Event<String>>{
        var status = it.getContentIfNotHandledOrReturnNull()?:return@Observer

        if(status !== io.socket.client.Socket.EVENT_CONNECT) {
            SocketManager.stopListeningToServer()
            _connectionState.postValue(Event(status))
        }
    }

    init {
        _participants.postValue(SessionData.currentRoom?.participants)
        _connectionState.postValue(SocketManager.connectionStatus.value)
        SocketManager.connectionStatus.observeForever(connectivityObserver)
        SocketManager.participantJoined.observeForever(participantJoinedObserver)
        SocketManager.playbackVideo.observeForever(videoPlaybackObserver)
        SocketManager.changedVideo.observeForever(videoChangedObserver)
        SocketManager.syncedVideo.observeForever(videoSyncedObserver)
        SocketManager.participantLeft.observeForever(participantLeftObserver)
    }

    override fun onCleared() {
        super.onCleared()
        SocketManager.connectionStatus.removeObserver(connectivityObserver)
        SocketManager.playbackVideo.removeObserver(videoPlaybackObserver)
        SocketManager.changedVideo.removeObserver(videoChangedObserver)
        SocketManager.syncedVideo.removeObserver(videoSyncedObserver)
        SocketManager.participantLeft.removeObserver(participantLeftObserver)
        SocketManager.participantJoined.removeObserver(participantJoinedObserver)
    }

    fun sendVideoPlaybackEvent(playbackStatus : String) {
        SocketManager.sendVideoStartedEvent(playbackStatus)
    }

    fun sendVideoChangedEvent(direction : String){
        SocketManager.sendVideoChangedEvent(direction)
    }

    fun sendVideoSyncedEvent(currentTimestamp : String){
        SocketManager.sendVideoSyncEvent(currentTimestamp)
    }

    fun leaveRoom(){
        SocketManager.stopListeningToServer()
    }
}