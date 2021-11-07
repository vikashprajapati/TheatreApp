package com.example.theatreapp.ui.fragment.streaming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.data.models.Message
import com.example.theatreapp.databinding.FragmentChatBinding
import com.example.theatreapp.ui.adapters.ChatMessagesAdapter
import com.example.theatreapp.ui.fragment.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    private val chatAdapter = ChatMessagesAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.viewModel = viewModel
        setUpViews()
        observeData()
    }

    override fun initViewModel(): ChatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

    override fun getViewBinding(): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)

    override fun observeData() {
        super.observeData()
        viewModel.messageList.observe(viewLifecycleOwner){
            chatAdapter.updateMessageList(it as ArrayList<Message>)
        }
    }

    override fun setUpViews() {
        super.setUpViews()
        binding?.messageRecyclerView.apply {
            this?.adapter = chatAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ChatFragment.
         */
        @JvmStatic
        fun newInstance() =
            ChatFragment()
    }
}