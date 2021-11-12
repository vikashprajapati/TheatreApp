package com.example.theatreapp.ui.fragment.streaming

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.theatreapp.R
import com.example.theatreapp.ui.adapters.ParticipantsRecyclerViewAdapter
import com.example.theatreapp.databinding.FragmentParticipantsListBinding
import com.example.theatreapp.data.models.response.joinroomresponse.ParticipantsItem
import com.example.theatreapp.ui.fragment.BaseFragment

/**
 * A fragment representing a list of Items.
 */
class ParticipantsFragment : BaseFragment<FragmentParticipantsListBinding, StreamingRoomFragmentViewModel>() {
    private val TAG = ParticipantsFragment::class.java.canonicalName
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
        viewModel.apply {
            participants.observe(viewLifecycleOwner, {
                participantAdapter.updateParticipantList(it)

                if(it.size > 1){
                    val msg = "${it[it.size - 1].name} ${resources.getString(R.string.participant_joined)}";
                    shortToast(msg)
                }
            })

            participantLeft.observe(viewLifecycleOwner){
                val participant = it.getContentIfNotHandledOrReturnNull()?:return@observe

                shortToast("${participant.name} left")
            }
        }
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