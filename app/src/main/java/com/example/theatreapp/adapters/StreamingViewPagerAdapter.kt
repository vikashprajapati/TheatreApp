package com.example.theatreapp.adapters

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.theatreapp.fragment.ChatFragment
import com.example.theatreapp.fragment.ParticipantsFragment

class StreamingViewPagerAdapter(var fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    private val titles = listOf<String>("Chats", "Participants")
    private val fragmentList =  listOf(ChatFragment.newInstance(), ParticipantsFragment.newInstance(1))
    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}