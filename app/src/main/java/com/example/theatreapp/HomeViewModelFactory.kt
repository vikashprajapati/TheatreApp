package com.example.theatreapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.connections.SocketManager
import com.example.theatreapp.viewmodel.HomeFragmentViewModel

class HomeViewModelFactory(private val socketManager: SocketManager) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SocketManager::class.java).newInstance(socketManager)
    }

}