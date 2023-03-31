package com.crystallake.demo.transition.browse

import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewTreeObserver
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.demo.MainActivityViewModel
import com.crystallake.demo.R
import com.crystallake.demo.BR
import com.crystallake.demo.databinding.ActivityBrowseShareElementTransitionBinding

class BrowseShareElementTransitionActivity :
    DataBindingActivity<ActivityBrowseShareElementTransitionBinding, MainActivityViewModel>() {

    companion object {
        const val EXTRA_START_POSITION = "start_position"
        const val EXTRA_CURRENT_POSITION = "current_position"

    }

    val adapter by lazy {
        MultiDataBindingAdapter()
    }

    val mCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            mReenterState?.let {
                val startingPosition = it.getInt(EXTRA_START_POSITION)
                val currentPosition = it.getInt(EXTRA_CURRENT_POSITION)
                val dataList = mViewModel.dataListLiveData.value
                if (startingPosition != currentPosition) {
                    val newTransitionName = "name${dataList!![currentPosition]}"
                    val newSharedElement = mBinding?.recycler?.findViewWithTag<View>(newTransitionName)
                    if (newSharedElement != null) {
                        names?.clear()
                        names?.add(newTransitionName)
                        sharedElements?.clear()
                        sharedElements?.put(newTransitionName, newSharedElement)
                    }
                }
                mReenterState = null
            } ?: kotlin.run {
                val navigationBar = findViewById<View>(android.R.id.navigationBarBackground)
                val statusBar = findViewById<View>(android.R.id.statusBarBackground)
                if (navigationBar != null) {
                    names?.add(navigationBar.transitionName)
                    sharedElements?.put(navigationBar.transitionName, navigationBar)
                }

                if (statusBar != null) {
                    names?.add(statusBar?.transitionName)
                    sharedElements?.put(statusBar.transitionName, statusBar)
                }
            }
        }
    }

    var mReenterState: Bundle? = null

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_browse_share_element_transition).addBindingParam(BR.adapter, adapter)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        println("onActivityReenter")
        mReenterState = Bundle(data?.extras)
        val startingPosition = mReenterState?.getInt(EXTRA_START_POSITION) ?: 0
        val currentPosition = mReenterState?.getInt(EXTRA_CURRENT_POSITION) ?: 0
        if (startingPosition != currentPosition) {
            mBinding?.recycler?.scrollToPosition(currentPosition)
        }
        window.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.share_grid_exit_transition)
        postponeEnterTransition()

        mBinding?.recycler?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                mBinding?.recycler?.viewTreeObserver?.removeOnPreDrawListener(this)
                mBinding?.recycler?.requestLayout()
                startPostponedEnterTransition()
                return true
            }
        })
    }

    override fun initData() {
        super.initData()
        mViewModel.initDataList()
        adapter.clear()
        mViewModel.dataListLiveData.value?.forEach {
            adapter.addItem(CardItem(it) { view, position ->
                val intent = Intent(this, ActivityBrowse::class.java)
                intent.putExtra(EXTRA_START_POSITION, position)
                val compat = ActivityOptions.makeSceneTransitionAnimation(this, view, view.transitionName)
                startActivity(intent, compat.toBundle())
            })
        }
    }

    private fun initShareElement() {
        setExitSharedElementCallback(mCallback)
    }


    override fun initObser() {
        super.initObser()
        initShareElement()
    }

}