package com.example.theatreapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.theatreapp.databinding.SearchResultItemBinding
import com.vikash.syncr_core.data.models.response.youtube.searchResults.VideosItem
import com.vikash.syncr_core.utils.SearchResultAdapterListener

class YoutubeSearchResultsAdapter(
    private var searchResultList : List<VideosItem?>?,
    private var listener : SearchResultAdapterListener
    ) : RecyclerView.Adapter<YoutubeSearchResultsAdapter.YoutubeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchResultItemBinding.inflate(inflater)
        return YoutubeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YoutubeItemViewHolder, position: Int) {
        val video = searchResultList?.get(position)
        holder.bind(video)
        holder.itemView.setOnClickListener{
            listener.videoSelected(video)
        }
    }

    override fun getItemCount(): Int = searchResultList?.size?:0

    fun updateSearchResultList(searchResultList: List<VideosItem?>?){
        this.searchResultList = searchResultList
        notifyDataSetChanged()
    }

    class YoutubeItemViewHolder(private val searchItem : SearchResultItemBinding) : RecyclerView.ViewHolder(searchItem.root) {
        fun bind(videoItem: VideosItem?){
            searchItem.video = videoItem
//            Glide.with(searchItem.root)
//                .load(videoItem?.snippet?.thumbnails?.medium?.url)
//                .fitCenter()
//                .into(searchItem.videoThumbnail)
            Glide.with(searchItem.root)
                .load(videoItem?.bestThumbnail?.url)
                .fitCenter()
                .into(searchItem.videoThumbnail)
        }
    }
}