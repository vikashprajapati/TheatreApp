package com.example.theatreapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.R
import com.example.theatreapp.databinding.ItemFragmentParticipantBinding

import com.example.theatreapp.models.participants.Participant

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ParticipantsRecyclerViewAdapter(
    private val participantList: ArrayList<Participant>
) : RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ParticipantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val binding = ItemFragmentParticipantBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ParticipantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val participant = participantList[position]
        holder.bind(participant)
    }

    override fun getItemCount(): Int = participantList.size

    inner class ParticipantViewHolder(private var binding: ItemFragmentParticipantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: Participant) {
            binding.participant = participant
        }
    }
}