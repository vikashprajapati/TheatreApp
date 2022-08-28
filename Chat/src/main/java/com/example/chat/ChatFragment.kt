package com.example.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.chat.databinding.FragmentChatBinding
import com.vikash.syncr_core.ui.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [ChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>() {
    private val chatAdapter = com.example.chat.ChatMessagesAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        binding?.apply {
            lifecycleOwner = this@ChatFragment
        }
        binding?.viewModel = viewModel
        return binding?.root
    }

    override fun initViewModel(): ChatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

    override fun getViewBinding(): FragmentChatBinding = FragmentChatBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.messageList.observe(viewLifecycleOwner){
            chatAdapter.updateMessageList(it as ArrayList<Message>)
        }
    }

    override fun setUpViews() {
        super.setUpViews()
        binding?.apply {
            messageRecyclerView.adapter = chatAdapter
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