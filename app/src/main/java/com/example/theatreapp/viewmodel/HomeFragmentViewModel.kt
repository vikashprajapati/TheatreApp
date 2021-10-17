package com.example.theatreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.theatreapp.models.room.Room

class HomeFragmentViewModel : ViewModel() {
    private val roomList : MutableLiveData<ArrayList<Room>> by lazy {
        MutableLiveData<ArrayList<Room>>()
    }

    init {
        loadAvailableRooms()
    }

    fun getRoomsList() : LiveData<ArrayList<Room>> {
        return roomList
    }

    private fun loadAvailableRooms(){
        var room1 = Room("", "Test Room 1", 2)
        var room2 = Room("", "Test Room 2", 5)
        var rooms = ArrayList<Room>()
        rooms.add(room1)
        rooms.add(room2)
        roomList.value = rooms
    }
}