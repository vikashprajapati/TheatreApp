package com.example.theatreapp.ui.fragment.streaming

import android.util.EventLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.theatreapp.data.connections.SocketManager
import com.example.theatreapp.data.models.Message
import com.example.theatreapp.utils.Event

class ChatViewModel : ViewModel() {
	private val _messageList = MutableLiveData<MutableList<Message>>()
	private val messageObserver = Observer<Event<Message>>{
		val message = it.getContentIfNotHandledOrReturnNull()?:return@Observer

		val msgList = _messageList.value
		msgList?.add(message)
		_messageList.postValue(msgList)
	}
	val messageList : LiveData<MutableList<Message>> get() = _messageList

	init {
		_messageList.value = mutableListOf()
		SocketManager.onMessage.observeForever(messageObserver)
	}

	override fun onCleared() {
		super.onCleared()
		SocketManager.onMessage.removeObserver(messageObserver)
	}
}