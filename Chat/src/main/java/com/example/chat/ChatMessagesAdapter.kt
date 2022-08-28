package com.example.chat

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ChatItemBinding
import com.vikash.syncr_core.data.SessionData

class ChatMessagesAdapter(private val messagesList : ArrayList<Message>) : RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
		return MessageViewHolder(ChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
		holder.bind(messagesList[position])
	}

	override fun getItemCount(): Int = messagesList.size

	@SuppressLint("NotifyDataSetChanged")
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