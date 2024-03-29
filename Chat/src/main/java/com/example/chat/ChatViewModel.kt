package com.example.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.vikash.syncr_core.data.connections.SocketManager
import com.vikash.syncr_core.utils.Event
import com.vikash.syncr_core.utils.Helpers

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
	private val onMessageObserver = Observer<Event<String>>{
		val message = it.getContentIfNotHandledOrReturnNull()?.run {
			Gson().fromJson(this, Message::class.java)
		}?:return@Observer

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
			val message = Message(from = "", message = _messageEditText.value!!, timeStamp = Helpers.getCurrentTime())
			SocketManager.sendChatMessage(Gson().toJson(message))
			clearMessageField()
		}
	}

	private fun clearMessageField(){
		_messageEditText.value = ""
	}
}