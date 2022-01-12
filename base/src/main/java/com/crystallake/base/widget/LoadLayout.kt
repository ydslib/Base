package com.crystallake.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils
import com.crystallake.base.R

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * Author: cretin
 */
class LoadLayout : BaseLoadLayout {
    var mLoadingViewId = R.layout.base_layout_loading
    var mFailedViewId = R.layout.base_load_error_layout
    val mNetErrViewId = R.layout.base_load_network_error_layout
    val mEmptyViewId = R.layout.base_load_empty_layout

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun createLoadingView(): View? {
        if (ActivityUtils.isActivityAlive(context)) {
            mLoadingView = LayoutInflater.from(context).inflate(mLoadingViewId, null)

            mLoadingView?.setOnClickListener {}
        }
        return mLoadingView
    }

    override fun createLoadFailedView(): View? {
        mFailedView = LayoutInflater.from(context).inflate(mFailedViewId, null)
        mFailedView?.setOnClickListener {}
        mFailedView?.findViewById<View>(R.id.reload)?.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                retry()
            } else {
                ToastUtils.showShort(R.string.base_str_net_err)
            }
        }
        return mFailedView
    }

    override fun createNetErrView(): View? {
        mNetErrView = LayoutInflater.from(context).inflate(mNetErrViewId, null)
        mNetErrView?.setOnClickListener(OnClickListener { v: View? -> })
        mNetErrView?.findViewById<View>(R.id.reload)?.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                retry()
            } else {
                ToastUtils.showShort(R.string.base_str_net_err)
            }
        }
        return mNetErrView
    }

    override fun createEmptyView(): View? {
        mEmptyView = LayoutInflater.from(context).inflate(mEmptyViewId, null)
        mEmptyView?.setOnClickListener { }
        mEmptyView?.findViewById<View>(R.id.load_go_other)?.setOnClickListener {
            loadEmptyListener?.invoke()
        }
        return mEmptyView
    }
}