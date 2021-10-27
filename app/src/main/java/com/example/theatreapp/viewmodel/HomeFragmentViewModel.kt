package com.example.theatreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatreapp.Event

class HomeFragmentViewModel : ViewModel() {
    private var room = MutableLiveData<String>()
    private var user = MutableLiveData<String>()

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
            joinRoom()
    }

    private fun joinRoom(){
    }
}