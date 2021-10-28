package com.example.theatreapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.Event
import com.example.theatreapp.connections.SocketManager
import io.socket.engineio.client.Socket

class HomeFragmentViewModel(private var socketManager: SocketManager) : ViewModel() {
    private val TAG: String = HomeFragmentViewModel::class.java.canonicalName
    private var room = MutableLiveData<String>()
    private var user = MutableLiveData<String>()
    private var connectivityObserver = Observer<Event<String>>{
        var status = it.getContentIfNotHandledOrReturnNull()

        if(status === io.socket.client.Socket.EVENT_CONNECT){
            joinRoom()
        }
    }

    init{
        socketManager.connectionState.observeForever(connectivityObserver)
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
    }

    private fun connectSocket(){
        socketManager.startListeningToServer()
    }

    private fun joinRoom() {
        Log.i(TAG, "joinRoom: ")
    }
}