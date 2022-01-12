package com.crystallake.base.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.crystallake.base.R
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.base.widget.LoadLayout
import com.crystallake.base.widget.State

abstract class BaseLoadingFragment<D : ViewDataBinding, VM : BaseViewModel> : BaseFragment<VM>() {

    var refreshing = MutableLiveData<Boolean>()

    //整个页面底层View
    private var rootView: View? = null

    //loadingView
    private var mLoadLayout: LoadLayout? = null

    var mBinding: D? = null

    /**
     * 自动设置加载状态
     */
    var autoSetLoadingState = true


    protected val dataBindingConfig: DataBindingConfig by lazy {
        initDataBindingConfig()
    }

    abstract fun initDataBindingConfig(): DataBindingConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.base_layout_base, container, false)
        mLoadLayout = rootView?.findViewById(R.id.ll_base)
        initOtherVM()

        rootView?.setOnTouchListener { _, _ -> true }

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, dataBindingConfig.layout, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(dataBindingConfig.vmVariableId, dataBindingConfig.stateViewModel)
        val bindingParams: SparseArray<Any> = dataBindingConfig.bindingParams
        for (i in 0 until bindingParams.size()) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
        }
        mBinding = binding as? D

        mLoadLayout?.setContentView(binding.root)
        mLoadLayout?.setLoadFailedRetryListener {
            showLoadingView()
            refreshPageData()
        }

        if (autoSetLoadingState) {
            binding.lifecycleOwner?.let { owner ->
                mViewModel?.requestLiveData?.observe(owner) {
                    val isRefreshing = refreshing.value
                    if (it == 0) {
                        if (isRefreshing == true) {
                            refreshing.postValue(false)
                        } else {
                            showContentView()
                        }
                        val failureNum = mViewModel.failureRequest.get()
                        if (failureNum > 0) {
                            showErrorView()
                        }
                        mViewModel.failureRequest.set(0)
                    } else {
                        if (isRefreshing == false && mLoadLayout?.layoutState != State.LOADING) {
                            showLoadingView()
                        }
                    }
                }
            }

        }

        return rootView
    }

    abstract fun refreshPageData()

    fun showLoadingView() {
        mLoadLayout?.layoutState = State.LOADING
    }

    fun showErrorView() {
        mLoadLayout?.layoutState = State.FAILED
    }

    fun showEmptyView() {
        mLoadLayout?.layoutState = State.EMPTY
    }

    fun showContentView() {
        mLoadLayout?.layoutState = State.SUCCESS
    }

    open fun initOtherVM() {

    }

    open fun smartRefreshData(refresh: () -> Unit) {
        refreshing.postValue(true)
        refresh.invoke()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }

}