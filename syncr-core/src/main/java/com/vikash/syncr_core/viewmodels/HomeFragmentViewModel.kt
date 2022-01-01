package com.vikash.syncr_core.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vikash.syncr_core.SyncrApplication
import com.vikash.syncr_core.data.connections.SocketManager
import com.vikash.syncr_core.data.models.response.joinroomresponse.JoinedRoomResponse
import com.vikash.syncr_core.utils.Event

class HomeFragmentViewModel() : ViewModel() {
    private val TAG = HomeFragmentViewModel::class.java.canonicalName
    private var _room = MutableLiveData<String>()
    private var _user = MutableLiveData<String>()
    private var _joinedRoomState = MutableLiveData<Event<JoinedRoomResponse>>()
    private var _connectionState = MutableLiveData<Event<String>>()
    val loading = MutableLiveData<Int>()
    val connectionState : LiveData<Event<String>> get() = _connectionState
    val joinRoomState : LiveData<Event<JoinedRoomResponse>> get() = _joinedRoomState

    private var connectivityObserver = Observer<String>{
        val status = it
        if(status === io.socket.client.Socket.EVENT_CONNECT){
            joinRoom()
        }else{
            SocketManager.stopListeningToServer()
            _connectionState.postValue(Event(status))
            loading.postValue(View.GONE)
        }
    }

    private var joinedRoomObserver = Observer<Event<JoinedRoomResponse>>{
        var joinedRoomResponse = it.getContentIfNotHandledOrReturnNull()
        loading.postValue(View.GONE)
        if(joinedRoomResponse == null){
            SocketManager.stopListeningToServer()
            _connectionState.postValue(Event("Failed to join room"))
            return@Observer;
        }
        _joinedRoomState.postValue(Event(joinedRoomResponse))
        clearFields()
    }

    init{
        loading.value = View.GONE
        clearFields()
        initObservers()
    }

    fun initObservers(){
        SocketManager.connectionStatus.observeForever(connectivityObserver)
        SocketManager.joinedRoomStatus.observeForever(joinedRoomObserver)
    }

    private fun clearFields(){
        _room.value = ""
        _user.value = ""
    }

    fun removeObservers(){
        SocketManager.joinedRoomStatus.removeObserver(joinedRoomObserver)
        SocketManager.connectionStatus.removeObserver(connectivityObserver)
    }

    var room : MutableLiveData<String>
        set(data){
            _room.value = data.value
        }
        get() = _room


    var user : MutableLiveData<String>
        set(data){
            _user.value = data.value
        }
        get() = _user


    var invalidInput = MutableLiveData<Event<String>>()

    fun validateInput() {
        if (_room.value.isNullOrEmpty() || _user.value.isNullOrEmpty())
            invalidInput.value = Event("Invalid Input")
        else{
            loading.postValue(View.VISIBLE)
            connectSocket()
        }
    }

    override fun onCleared() {
        super.onCleared()
        removeObservers()
    }

    private fun connectSocket(){
        SocketManager.startListeningToServer()
    }

    private fun joinRoom() {
        Log.i(TAG, "joinRoom: ")
        SocketManager.sendJoinRoom(_user.value!!, _room.value!!)
    }
}