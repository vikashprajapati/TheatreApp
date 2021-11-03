package com.example.theatreapp.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.App
import com.example.theatreapp.R
import com.example.theatreapp.utils.Event
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData

class HomeFragmentViewModel(private var socketManager: SocketManager) : ViewModel() {
    private val TAG: String = HomeFragmentViewModel::class.java.canonicalName
    private var _room = MutableLiveData<String>()
    private var _user = MutableLiveData<String>()
    val loading = MutableLiveData<Int>()
    val connectionState = MutableLiveData<String>()
    val joinRoomState = MutableLiveData<String>()

    private var connectivityObserver = Observer<Event<String>>{
        var status = it.getContentIfNotHandledOrReturnNull()

        if(status === io.socket.client.Socket.EVENT_CONNECT){
            joinRoom()
        }else{
            socketManager.stopListeningToServer()
            connectionState.postValue(status)
            loading.postValue(View.GONE)
        }
    }

    private var joinedRoomObserver = Observer<Event<JoinedRoomResponse>>{
        var joinedRoomResponse = it.getContentIfNotHandledOrReturnNull()
        loading.postValue(View.GONE)
        if(joinedRoomResponse == null){
            socketManager.stopListeningToServer()
            connectionState.postValue("Failed to join room")
            return@Observer;
        }
        joinRoomState.postValue(App.gson.toJson(joinedRoomResponse))
        updateSessionData(joinedRoomResponse)
        clearFields()
    }

    init{
        loading.value = View.GONE
        clearFields()
        socketManager.connectionState.observeForever(connectivityObserver)
        socketManager.joinedRoomState.observeForever(joinedRoomObserver)
    }

    private fun clearFields(){
        _room.value = ""
        _user.value = ""
    }

    private fun updateSessionData(response: JoinedRoomResponse){
        SessionData.localUser = User(_user.value!!).apply { id = response.room.host }
        SessionData.currentRoom = response.room
        Log.i(TAG, "updateSessionData: $SessionData")
    }

    var room : String
        set(value){
            _room.value = value
        }
        get() = _room.value?:""


    var user : String
        set(value){
            _user.value = value
        }
        get() = _user.value?:""


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
        socketManager.connectionState.removeObserver(connectivityObserver)
        socketManager.joinedRoomState.removeObserver(joinedRoomObserver)
    }

    private fun connectSocket(){
        socketManager.startListeningToServer()
    }

    private fun joinRoom() {
        Log.i(TAG, "joinRoom: ")
        socketManager.joinRoom(_user.value!!, _room.value!!)
    }
}