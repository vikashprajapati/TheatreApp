package com.example.theatreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeFragmentViewModel : ViewModel() {
    private lateinit var room : MutableLiveData<String>
    private lateinit var user : MutableLiveData<String>

    var roomName : String
        get() = room?.value?:""
        set(value){
            room.value = value
        }

    var userName : String
        get() = user?.value?:""
        set(value){
            user.value = value
        }

    fun validateInput() = if(room.value.isNullOrEmpty() || user.value.isNullOrEmpty()) invalidInput() else joinRoom()

    private fun invalidInput(){

    }

    private fun joinRoom(){

    }
}