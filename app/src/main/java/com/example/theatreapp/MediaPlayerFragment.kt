package com.example.theatreapp

import android.nfc.Tag
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.example.theatreapp.databinding.FragmentMediaPlayerBinding
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MediaPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MediaPlayerFragment : Fragment() {
    private lateinit var binding: FragmentMediaPlayerBinding
    private lateinit var libvlc: LibVLC
    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMediaPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupVlcMediaPlayer()
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer.attachViews(binding.videoPlayerLayout, null, false, false)
//        var media : Media = Media(libvlc, Uri.parse("rtsp://hdvcuser:PlayEP1234@p2ptrials.ddns.net:8804"))
        var media : Media = Media(libvlc, context?.assets?.openFd("videoplayback.mp4"))
        mediaPlayer.media = media
        media.release()

        mediaPlayer.play()
    }

    private fun setupVlcMediaPlayer() {
        // setup player
        val args = ArrayList<String>()
        args.add("-vvv")
        libvlc =  LibVLC(context, args)
        mediaPlayer= MediaPlayer(libvlc)

        // setup video controller
        val videoController = MediaController(context)
        videoController.setMediaPlayer(PlayerController(requireContext(), mediaPlayer))
        videoController.setAnchorView(binding.videoPlayerLayout)
        binding.videoPlayerLayout.setOnClickListener{
            videoController.show(5000)
        }
    }

    companion object {
        val TAG : String = MediaPlayerFragment.javaClass.canonicalName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MediaPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = MediaPlayerFragment()

    }
}