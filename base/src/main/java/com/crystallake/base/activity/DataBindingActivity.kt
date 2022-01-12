package com.crystallake.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.crystallake.base.config.DataBindingConfig

abstract class DataBindingActivity<D : ViewDataBinding, VM : ViewModel> : BaseActivity<VM>() {

    protected val dataBindingConfig: DataBindingConfig by lazy {
        initDataBindingConfig()
    }

    abstract fun initDataBindingConfig(): DataBindingConfig

    var mBinding: D? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initOtherVM()

        val binding: ViewDataBinding =
            DataBindingUtil.setContentView(this, dataBindingConfig.layout)
        binding.lifecycleOwner = this
        binding.setVariable(
            dataBindingConfig.vmVariableId,
            dataBindingConfig.stateViewModel
        )
        val bindingParams = dataBindingConfig.bindingParams
        for (i in 0 until bindingParams.size()){
            binding.setVariable(bindingParams.keyAt(i),bindingParams.valueAt(i))
        }

        mBinding = binding as D

        initObser()
        initData()
    }

    open fun initData() {

    }

    open fun initObser() {

    }

    open fun initOtherVM() {

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }

}