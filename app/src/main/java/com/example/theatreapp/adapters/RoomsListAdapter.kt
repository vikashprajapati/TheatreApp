package com.example.theatreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.R
import com.example.theatreapp.databinding.ItemRoomBinding
import com.example.theatreapp.models.room.Room

class RoomsListAdapter(
    var context: Context,
    var roomsList: List<Room>?
) : RecyclerView.Adapter<RoomsListAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
//        var binding = ItemRoomBinding.inflate(LayoutInflater.from(context))
        var binding : ItemRoomBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_room,
            parent,
            false
        )
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        var room = roomsList?.get(position)
        holder.bind(room)
    }

    override fun getItemCount(): Int = roomsList ?. size ?: 0

    class RoomViewHolder(private val itemBinding: ItemRoomBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(room: Room?) {
            itemBinding.room = room?: Room("0990", "Test Room", 3)
        }
    }
}