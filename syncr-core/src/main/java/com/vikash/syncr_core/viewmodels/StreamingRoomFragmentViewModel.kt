package com.vikash.syncr_core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vikash.syncr_core.data.connections.SocketManager
import com.vikash.syncr_core.data.models.response.joinroomresponse.ParticipantsItem
import com.vikash.syncr_core.data.SessionData
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoChanged
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoPlayback
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoSynced
import com.vikash.syncr_core.utils.Event

class StreamingRoomFragmentViewModel : ViewModel() {
    // temporarily we are keeping only video url, later we will be needing more video details
    private val activeVideoUrl = MutableLiveData<String>()
    private var _connectionState = MutableLiveData<String>()
    private val _participants = MutableLiveData<MutableList<ParticipantsItem>>()
    private val _videoPlayback = MutableLiveData<Event<VideoPlayback>>()
    private val _videoChanged = MutableLiveData<Event<VideoChanged>>()
    private val _videoSynced = MutableLiveData<Event<VideoSynced>>()
    private val _participantLeft = MutableLiveData<Event<ParticipantsItem>>()
    private val _participantJoined = MutableLiveData<Event<ParticipantsItem>>()
    private val _fullScreenLayout = MutableLiveData<Boolean>()

    // participants to be changed to livedata of list<participantItem>
    var participants : LiveData<List<ParticipantsItem>> = _participants as LiveData<List<ParticipantsItem>>
    var videoPlayback : LiveData<Event<VideoPlayback>> = _videoPlayback
    var videoChanged : LiveData<Event<VideoChanged>> = _videoChanged
    var videoSynced : LiveData<Event<VideoSynced>> = _videoSynced
    var connectionState : LiveData<String> = _connectionState
    var participantLeft : LiveData<Event<ParticipantsItem>> = _participantLeft
    var participantJoined : LiveData<Event<ParticipantsItem>> = _participantJoined
    var fullScreenLayout : MutableLiveData<Boolean> = _fullScreenLayout

    private var participantJoinedObserver = Observer<Event<ParticipantsItem>>{
        val participant = it.getContentIfNotHandledOrReturnNull() ?: return@Observer
        // participant added previously in SessionData
        _participants.postValue(SessionData.currentRoom?.participants)
        _participantJoined.postValue(Event(participant))
    }

    private var videoPlaybackObserver = Observer<Event<VideoPlayback>>{
        _videoPlayback.postValue(it)
    }

    private var videoChangedObserver = Observer<Event<VideoChanged>>{
        _videoChanged.postValue(it)
    }

    private var videoSyncedObserver = Observer<Event<VideoSynced>>{
        _videoSynced.postValue(it)
    }

    private var participantLeftObserver = Observer<Event<ParticipantsItem>>{
        _participantLeft.postValue(it)
        _participants.postValue(SessionData.currentRoom?.participants)
    }

    private var connectivityObserver = Observer<String>{
        var status = it

        if(status !== io.socket.client.Socket.EVENT_CONNECT) {
            SocketManager.stopListeningToServer()
            _connectionState.postValue(status)
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

    fun sendVideoJumpEvent(direction: String, timeStamp: Long){
        SocketManager.sendVideoJumpEvent(direction, timeStamp)
    }

    fun sendVideoSyncedEvent(currentTimestamp : String){
        SocketManager.sendVideoSyncEvent(currentTimestamp)
    }

    fun leaveRoom(){
        SocketManager.stopListeningToServer()
    }
}