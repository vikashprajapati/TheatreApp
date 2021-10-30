package com.example.theatreapp.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.App
import com.example.theatreapp.utils.Event
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.storage.SessionData

class HomeFragmentViewModel(private var socketManager: SocketManager) : ViewModel() {
    private val TAG: String = HomeFragmentViewModel::class.java.canonicalName
    private var room = MutableLiveData<String>()
    private var user = MutableLiveData<String>()
    public val loading = MutableLiveData<Int>()
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
        room.value = ""
        user.value = ""
    }

    private fun updateSessionData(response: JoinedRoomResponse){
        SessionData.localUser = User(user.value!!).apply { id = response.room.host }
        SessionData.currentRoom = response.room
        SessionData.participants = response.room.participants as MutableList<ParticipantsItem>
        Log.i(TAG, "updateSessionData: $SessionData")
    }

    var roomName : String
        set(value){
            room.value = value
        }
        get() = room.value?:""


    var userName : String
        set(value){
            user.value = value
        }
        get() = user.value?:""


    var invalidInput = MutableLiveData<Event<String>>()

    fun validateInput() {
        if (room.value.isNullOrEmpty() || user.value.isNullOrEmpty())
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
        socketManager.joinRoom(user.value!!, room.value!!)
    }
}