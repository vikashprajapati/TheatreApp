package com.example.theatreapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.chat.ChatFragment
import com.example.theatreapp.ui.fragment.streaming.viewpager.ParticipantsFragment
import com.example.theatreapp.ui.fragment.streaming.viewpager.RoomFragment

class StreamingViewPagerAdapter(fragmentManager: FragmentManager) :
	FragmentStatePagerAdapter(fragmentManager) {
	private val titles = listOf<String>("Room", "Chats", "Participants")
	private val fragmentList = listOf(
        RoomFragment.newInstance(),
        com.example.chat.ChatFragment.newInstance(),
        ParticipantsFragment.newInstance(1)
    )

	override fun getCount(): Int = fragmentList.size

	override fun getItem(position: Int): Fragment = fragmentList[position]

	override fun getPageTitle(position: Int): CharSequence? = titles[position]
}