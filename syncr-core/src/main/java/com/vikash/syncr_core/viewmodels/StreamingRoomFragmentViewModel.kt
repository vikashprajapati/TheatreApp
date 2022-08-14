package com.vikash.syncr_core.viewmodels

import androidx.lifecycle.*
import com.vikash.syncr_core.data.connections.SocketManager
import com.vikash.syncr_core.data.models.response.joinroomresponse.ParticipantsItem
import com.vikash.syncr_core.data.SessionData
import com.vikash.syncr_core.data.models.videoplaybackevents.NewVideoSelected
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoChanged
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoPlayback
import com.vikash.syncr_core.data.models.videoplaybackevents.VideoSynced
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.events.VideoUrlExtractedEvent
import com.vikash.syncr_core.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@HiltViewModel
class StreamingRoomFragmentViewModel @Inject constructor (private val youtubeRepository: YoutubeRepository) : ViewModel() {

    private val _activeVideoUrl = MutableLiveData<String>()
    var activeVideoUrl : MutableLiveData<String>
        get() = _activeVideoUrl
        set(url) {
            _activeVideoUrl.postValue(url.value)
            // call youtube video url extraction
            extractYoutubeUrl(url.value!!)
        }

    private val _participants = MutableLiveData<MutableList<ParticipantsItem>>()
    var participants: LiveData<List<ParticipantsItem>> = _participants as LiveData<List<ParticipantsItem>>

    private val _videoPlayback = MutableLiveData<Event<VideoPlayback>>()
    var videoPlayback: LiveData<Event<VideoPlayback>> = _videoPlayback

    private val _videoSynced = MutableLiveData<Event<VideoSynced>>()
    var videoSynced: LiveData<Event<VideoSynced>> = _videoSynced

    private val _videoChanged = MutableLiveData<Event<VideoChanged>>()
    var videoChanged: LiveData<Event<VideoChanged>> = _videoChanged

    private var _connectionState = MutableLiveData<String>()
    var connectionState: LiveData<String> = _connectionState

    private val _participantLeft = MutableLiveData<Event<ParticipantsItem>>()
    var participantLeft: LiveData<Event<ParticipantsItem>> = _participantLeft

    private val _participantJoined = MutableLiveData<Event<ParticipantsItem>>()
    var participantJoined: LiveData<Event<ParticipantsItem>> = _participantJoined

    private val _fullScreenLayout = MutableLiveData<Boolean>()
    var fullScreenLayout: MutableLiveData<Boolean> = _fullScreenLayout

    private val _participantArrived = MutableLiveData<Event<ParticipantsItem>>()
    var participantArrived: LiveData<Event<ParticipantsItem>> = _participantArrived

    private var _newVideo = MutableLiveData<Event<NewVideoSelected>>()
    var newVideoSelected: LiveData<Event<NewVideoSelected>> = _newVideo

    private val _bufferingStatus = MutableLiveData<Boolean>()
    var bufferingStatus : LiveData<Boolean> = _bufferingStatus

    var videoSyncSlider = MutableLiveData<Float>(0.0F)

    private var _mediaPlayerFragmentViewHeight = MutableLiveData<Int>()
    var mediaPlayerFragmentViewHeight: MutableLiveData<Int>
        get() = _mediaPlayerFragmentViewHeight
        set(value) {
            _mediaPlayerFragmentViewHeight = value
        }

    private var newVideoSelectedObserver = Observer<Event<NewVideoSelected>> {
        val videoDetails = it.getContentIfNotHandledOrReturnNull()?:return@Observer
        _newVideo.postValue(Event(videoDetails))
        _activeVideoUrl.postValue(videoDetails.videoUrl)
    }

    private var participantJoinedObserver = Observer<Event<ParticipantsItem>> {
        val participant = it.getContentIfNotHandledOrReturnNull() ?: return@Observer
        // participant added previously in SessionData
        _participants.postValue(SessionData.currentRoom?.participants)
        _participantJoined.postValue(Event(participant))
        _participantArrived.postValue(Event(participant))
    }

    private var bufferingStatusObserver = Observer<Boolean>{
        _bufferingStatus.postValue(it)
    }

    private var videoPlaybackObserver = Observer<Event<VideoPlayback>> {
        _videoPlayback.postValue(it)
    }

    private var videoChangedObserver = Observer<Event<VideoChanged>> {
        _videoChanged.postValue(it)
    }

    private var videoSyncedObserver = Observer<Event<VideoSynced>> {
        _videoSynced.postValue(it)
    }

    private var participantLeftObserver = Observer<Event<ParticipantsItem>> {
        _participantLeft.postValue(it)
        _participants.postValue(SessionData.currentRoom?.participants)
    }

    private var connectivityObserver = Observer<String> {
        var status = it

        if (status !== io.socket.client.Socket.EVENT_CONNECT) {
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
        SocketManager.onNewVideo.observeForever(newVideoSelectedObserver)
        SocketManager.bufferingStatus.observeForever(bufferingStatusObserver)

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    private fun extractYoutubeUrl(youtubeUrl : String) = viewModelScope.launch{
        val youtubePlaybackUrl = youtubeRepository.extractVideoUrl(youtubeUrl)
        if(youtubePlaybackUrl.isNotEmpty()){
            withContext(Dispatchers.Main){
                EventBus.getDefault().post(VideoUrlExtractedEvent(youtubePlaybackUrl, ""))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        SocketManager.connectionStatus.removeObserver(connectivityObserver)
        SocketManager.playbackVideo.removeObserver(videoPlaybackObserver)
        SocketManager.changedVideo.removeObserver(videoChangedObserver)
        SocketManager.syncedVideo.removeObserver(videoSyncedObserver)
        SocketManager.participantLeft.removeObserver(participantLeftObserver)
        SocketManager.participantJoined.removeObserver(participantJoinedObserver)
        SocketManager.bufferingStatus.removeObserver(bufferingStatusObserver)

        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
    }

    fun sendVideoPlaybackEvent(playbackStatus: String) {
        SocketManager.sendVideoStartedEvent(playbackStatus)
    }

    fun sendVideoJumpEvent(direction: String, timeStamp: Long) {
        SocketManager.sendVideoJumpEvent(direction, timeStamp)
    }

    fun sendVideoSyncedEvent(currentTimestamp: String) {
        SocketManager.sendVideoSyncEvent(currentTimestamp)
    }

    fun sendVideoBufferingEvent(bufferingStatus : Boolean){
        SocketManager.sendVideoBufferingEvent(bufferingStatus)
    }

    fun leaveRoom() {
        SocketManager.stopListeningToServer()
    }

    @Subscribe fun videoUrlExtracted(videoUrlExtractedEvent: VideoUrlExtractedEvent){
        _newVideo.postValue(Event(NewVideoSelected(
            videoUrlExtractedEvent.videoUrl,
            videoUrlExtractedEvent.videoTitle
        )))
    }
}