package com.example.theatreapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.adapters.ParticipantsRecyclerViewAdapter
import com.example.theatreapp.databinding.FragmentParticipantsListBinding
import com.example.theatreapp.models.participants.Participant
import com.example.theatreapp.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.viewmodel.StreamingRoomFragmentViewModel

/**
 * A fragment representing a list of Items.
 */
class ParticipantsFragment : BaseFragment<FragmentParticipantsListBinding, StreamingRoomFragmentViewModel>() {
    private val TAG = ParticipantsFragment.javaClass.canonicalName
    private var columnCount = 1
    private var participantList : List<ParticipantsItem>? = null
    private val participantAdapter : ParticipantsRecyclerViewAdapter = ParticipantsRecyclerViewAdapter(participantList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }

    override fun setUpViews() {
        super.setUpViews()
        binding?.participantRecyclerView?.apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = participantAdapter
        }
    }

    override fun initViewModel(): StreamingRoomFragmentViewModel {
        return ViewModelProvider(requireActivity()).get(StreamingRoomFragmentViewModel::class.java)
    }

    override fun getViewBinding(): FragmentParticipantsListBinding = FragmentParticipantsListBinding.inflate(layoutInflater)

    override fun observeData() {
        super.observeData()
        viewModel.participantsList.observe(viewLifecycleOwner, {
            participantAdapter.updateParticipantList(it)
        })
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance(columnCount: Int = 1) =
            ParticipantsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}