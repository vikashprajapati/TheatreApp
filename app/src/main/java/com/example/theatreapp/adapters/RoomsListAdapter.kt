package com.example.theatreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.R
import com.example.theatreapp.databinding.ItemRoomBinding
import com.example.theatreapp.models.room.Room

class RoomsListAdapter(
    var context: Context,
    var roomsList: List<Room>,
    var roomSelectedListener: (Room) -> Unit
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
        holder.bind(roomsList[position], roomSelectedListener)
    }

    override fun getItemCount(): Int = roomsList.size

    class RoomViewHolder(private val itemBinding: ItemRoomBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun bind(room: Room, roomSelectedListener: (Room) -> Unit) {
            itemBinding.room = room
            itemBinding.root.setOnClickListener{
                roomSelectedListener(room)
            }
        }
    }
}