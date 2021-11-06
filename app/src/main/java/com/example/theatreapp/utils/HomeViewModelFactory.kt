package com.example.theatreapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.data.connections.SocketManager

class HomeViewModelFactory(private val socketManager: SocketManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SocketManager::class.java).newInstance(socketManager)
    }

}