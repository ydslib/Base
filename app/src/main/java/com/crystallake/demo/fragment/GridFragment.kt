package com.crystallake.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.BR
import com.crystallake.demo.MainActivityViewModel
import com.crystallake.demo.R
import com.crystallake.demo.adapter.ImageAdapter
import com.crystallake.demo.databinding.FragmentGridBinding

class GridFragment : DataBindingFragment<FragmentGridBinding, BaseViewModel>() {


    val mActivityViewModel by lazy {
        getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    private val imageAdapter by lazy {
        ImageAdapter(this){
        }
    }


    override fun lazyLoadData() {
        mActivityViewModel?.initDataList()
    }

    override fun createObserver() {
        mActivityViewModel?.dataListLiveData?.observe(this) {
            imageAdapter.setData(it)
            imageAdapter.notifyDataSetChanged()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
//        imageAdapter.onItemClickListener = object : SingleDataBindingAdapter.OnItemClickListener {
//            override fun onItemClick(view: View?, position: Int) {
//                mActivityViewModel?.currentPositionLiveData?.value = position
//                mActivityViewModel?.navigationFragment?.value = MainActivityViewModel.IMAGE_PAGER_FRAGMENT
//            }
//        }
        scrollToPosition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        prepareTransitions()
        postponeEnterTransition()
        return view
    }


    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_grid)
            .addBindingParam(BR.gridLayoutManager, GridLayoutManager(context, 2))
            .addBindingParam(BR.adapter, imageAdapter)
    }

    private fun scrollToPosition() {
        mBinding?.recycler?.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                mBinding?.recycler?.removeOnLayoutChangeListener(this)
                val layoutManager = mBinding?.recycler?.layoutManager

                val view = layoutManager?.findViewByPosition(MainActivityViewModel.currentPosition)

                if (view == null || !layoutManager.isViewPartiallyVisible(view, true, false)) {
                    mBinding?.recycler?.post {
                        layoutManager?.scrollToPosition(MainActivityViewModel.currentPosition)
                    }
                }
            }

        })
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                val selectedViewHolder =
                    mBinding?.recycler?.findViewHolderForAdapterPosition(MainActivityViewModel.currentPosition)
                selectedViewHolder?.let {
                    sharedElements?.put(names!![0], selectedViewHolder.itemView.findViewById(R.id.image))
                }

            }
        })
    }


}