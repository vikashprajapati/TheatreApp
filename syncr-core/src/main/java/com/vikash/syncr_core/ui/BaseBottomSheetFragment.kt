package com.vikash.syncr_core.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple [Fragment] subclass.
 * Use the [BaseBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseBottomSheetFragment<VBinding : ViewBinding, VModel : androidx.lifecycle.ViewModel> : BottomSheetDialogFragment() {
    protected lateinit var viewModel: VModel
    protected abstract fun initViewModel() : VModel

    protected var binding : VBinding? = null
    protected abstract fun getViewBinding() : VBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return binding?.root
    }

    private fun init(){
        binding = getViewBinding()
        viewModel = initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }


    open fun setUpViews() {}

    open fun observeData() {}

    protected fun shortToast(message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun shortToast(resId : Int){
        Toast.makeText(context, resources.getText(resId), Toast.LENGTH_SHORT).show()
    }

    protected fun longToast(message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    protected fun longToast(resId : Int){
        Toast.makeText(context, resources.getText(resId), Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}