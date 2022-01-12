package com.crystallake.base.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.crystallake.base.config.DataBindingConfig

abstract class DataBindingFragment<D : ViewDataBinding, VM : ViewModel> : BaseFragment<VM>() {

    var mBinding: D? = null

    protected val dataBindingConfig: DataBindingConfig by lazy {
        initDataBindingConfig()
    }

    abstract fun initDataBindingConfig(): DataBindingConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initOtherVM()
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, dataBindingConfig.layout, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)
        val bindingParams: SparseArray<Any> = dataBindingConfig.bindingParams
        for (i in 0 until bindingParams.size()) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
        }
        mBinding = binding as D
        return binding.root
    }

    open fun initOtherVM() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }
}