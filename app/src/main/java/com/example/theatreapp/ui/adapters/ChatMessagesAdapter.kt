package com.example.theatreapp.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.data.SessionData
import com.example.theatreapp.data.models.Message
import com.example.theatreapp.databinding.ChatItemBinding

class ChatMessagesAdapter(private val messagesList : ArrayList<Message>) : RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
		return MessageViewHolder(ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
		holder.bind(messagesList[position])
	}

	override fun getItemCount(): Int = messagesList.size

	fun updateMessageList(messages: ArrayList<Message>){
		messagesList.clear()
		messagesList.addAll(messages)
		notifyDataSetChanged()
	}

	inner class MessageViewHolder(private val binding : ChatItemBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(message : Message){
			binding.message = message
			if(message.from == SessionData.localUser?.id)
				binding.chatItemLinearLayout.gravity = Gravity.RIGHT
			else
				binding.chatItemLinearLayout.gravity = Gravity.LEFT
			binding.executePendingBindings()
		}
	}
}