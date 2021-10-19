package com.example.theatreapp.adapters

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter

class StreamingViewPagerAdapter(
    var fragmentManager: FragmentManager,
    var fragmentList: List<Fragment>
) : FragmentStatePagerAdapter(fragmentManager){
    private val titles = listOf<String>("Chats", "Participants")
    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}