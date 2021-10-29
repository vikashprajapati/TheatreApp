package com.example.theatreapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.App
import com.example.theatreapp.Event
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.models.requests.JoinRoomRequest
import com.example.theatreapp.models.requests.Room
import com.example.theatreapp.models.requests.User
import com.example.theatreapp.models.response.joinroomresponse.JoinedRoomResponse
import com.google.gson.JsonObject
import io.socket.engineio.client.Socket

class HomeFragmentViewModel(private var socketManager: SocketManager) : ViewModel() {
    private val TAG: String = HomeFragmentViewModel::class.java.canonicalName
    private var room = MutableLiveData<String>()
    private var user = MutableLiveData<String>()
    val connectionState = MutableLiveData<String>()
    val joinRoomState = MutableLiveData<String>()

    private var connectivityObserver = Observer<Event<String>>{
        var status = it.getContentIfNotHandledOrReturnNull()

        if(status === io.socket.client.Socket.EVENT_CONNECT){
            joinRoom()
        }else{
            connectionState.postValue(status)
            socketManager.stopListeningToServer()
        }
    }

    private var joinedRoomObserver = Observer<Event<JoinedRoomResponse>>{
        var joinedRoomResponse = it.getContentIfNotHandledOrReturnNull()
        if(joinedRoomResponse == null){
            socketManager.stopListeningToServer()
            return@Observer;
        }
        joinRoomState.postValue(App.gson.toJson(joinedRoomResponse))
        clearFields()
    }

    init{
        clearFields()
        socketManager.connectionState.observeForever(connectivityObserver)
        socketManager.joinedRoomState.observeForever(joinedRoomObserver)
    }

    private fun clearFields(){
        room.value = ""
        user.value = ""
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
        else
            connectSocket()
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