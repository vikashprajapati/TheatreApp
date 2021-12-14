package com.example.theatreapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.ActivityMainBinding
import com.example.theatreapp.ui.fragment.streaming.MediaPlayerFragment
import com.vikash.syncr_core.data.models.response.youtube.searchResults.ItemsItem

class MainActivity :
    AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHostfragment).navigateUp()

    fun notifyMediaPlayerFragmentToChangeVideo(videoItem: ItemsItem?){
        findNavController(R.id.navHostfragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostfragment)
        val fragments = navHostFragment?.childFragmentManager?.fragments;
        val size = fragments?.size?:0
        var mediaPlayerFragment : MediaPlayerFragment? = null
        for (i in 0 until size){
            if(fragments?.get(i) is MediaPlayerFragment){
                mediaPlayerFragment = fragments?.get(i) as MediaPlayerFragment
                break;
            }
        }

        if(mediaPlayerFragment != null){
            mediaPlayerFragment.changeVideo(videoItem)
        }
    }

}