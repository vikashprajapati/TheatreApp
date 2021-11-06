package com.example.theatreapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.databinding.ItemFragmentParticipantBinding

import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem

/**
 * [RecyclerView.Adapter] that can display a [ParticipantsItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ParticipantsRecyclerViewAdapter(
    private var participantList: List<ParticipantsItem>?
) : RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val binding = ItemFragmentParticipantBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = participantList?.get(position)
        if (participant != null) {
            holder.bind(participant)
        }
    }

    override fun getItemCount(): Int = participantList?.size?:0

    fun updateParticipantList(participantList: List<ParticipantsItem>?){
        if(participantList != null){
            this.participantList = participantList
            notifyDataSetChanged()
        }
    }

    inner class ParticipantViewHolder(private var binding: ItemFragmentParticipantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: ParticipantsItem?) {
            binding.participant = participant
        }
    }
}