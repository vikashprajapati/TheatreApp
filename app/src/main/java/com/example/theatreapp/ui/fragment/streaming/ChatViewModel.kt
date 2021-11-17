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
	private val _messageEditText = MutableLiveData<String>()
	private val _messageList = MutableLiveData<MutableList<Message>>()
	private val invalidInput = MutableLiveData<Event<String>>()

	val messageList : LiveData<MutableList<Message>> get() = _messageList
	var messageText : MutableLiveData<String>
		get() = _messageEditText
		set(data) {
			_messageEditText.value = data.value
		}
	private val onMessageObserver = Observer<Event<Message>>{
		val message = it.getContentIfNotHandledOrReturnNull()?:return@Observer

		val msgList = _messageList.value
		msgList?.add(message)
		_messageList.postValue(msgList)
	}

	init {
		clearMessageField()
		_messageList.value = mutableListOf()
		SocketManager.onMessage.observeForever(onMessageObserver)
	}

	override fun onCleared() {
		super.onCleared()
		SocketManager.onMessage.removeObserver(onMessageObserver)
	}

	fun validateInput(){
		if(_messageEditText.value.isNullOrEmpty()) invalidInput.postValue(Event("Message typed is empty"))
		else{
			// pass the msg body to socket manager
			SocketManager.sendChatMessage(_messageEditText.value!!)
			clearMessageField()
		}
	}

	private fun clearMessageField(){
		_messageEditText.value = ""
	}
}