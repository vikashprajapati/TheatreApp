package com.example.theatreapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.theatreapp.databinding.SearchResultItemBinding
import com.vikash.syncr_core.data.models.response.youtube.searchResults.ItemsItem

class YoutubeSearchResultsAdapter(
    private var searchResultList : List<ItemsItem?>?
    ) : RecyclerView.Adapter<YoutubeSearchResultsAdapter.YoutubeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchResultItemBinding.inflate(inflater)
        return YoutubeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YoutubeItemViewHolder, position: Int) {
        val video = searchResultList?.get(position)
        holder.bind(video)
    }

    override fun getItemCount(): Int = searchResultList?.size?:0

    fun updateSearchResultList(searchResultList: List<ItemsItem?>?){
        this.searchResultList = searchResultList
        notifyDataSetChanged()
    }

    class YoutubeItemViewHolder(private val searchItem : SearchResultItemBinding) : RecyclerView.ViewHolder(searchItem.root) {
        fun bind(videoItem: ItemsItem?){
            searchItem.video = videoItem
        }
    }
}