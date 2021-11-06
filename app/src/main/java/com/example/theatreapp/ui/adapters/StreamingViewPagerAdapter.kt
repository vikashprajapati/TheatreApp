package com.example.theatreapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.theatreapp.ui.fragment.streaming.ChatFragment
import com.example.theatreapp.ui.fragment.streaming.ParticipantsFragment

class StreamingViewPagerAdapter(var fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    private val titles = listOf<String>("Chats", "Participants")
    private val fragmentList =  listOf(ChatFragment.newInstance(), ParticipantsFragment.newInstance(1))
    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}