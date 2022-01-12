package com.crystallake.base.widget

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.NetworkUtils

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * Author: cretin
 * Date: 2021/9/7 4:01 下午
 */
abstract class BaseLoadLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), State {
    /**
     * 加载动画类
     */
    private val mAnimationDrawable: AnimationDrawable? = null

    /**
     * 加载成功
     */
    private var mSuccessView: View? = null

    /**
     * 加载中
     */
    @JvmField
    protected var mLoadingView: View? = null

    /**
     * 加载失败
     */
    @JvmField
    protected var mFailedView: View? = null

    /**
     * 网络错误
     */
    @JvmField
    protected var mNetErrView: View? = null

    /**
     * 空视图
     */
    @JvmField
    protected var mEmptyView: View? = null

    //    protected View mNoDataView;
    private var state = State.SUCCESS


    private var mContext: Context? = null


    /**
     * 加载中
     *
     * @return
     */
    protected abstract fun createLoadingView(): View?

    /**
     * 加载失败
     *
     * @return
     */
    protected abstract fun createLoadFailedView(): View?

    /**
     * 网络错误
     *
     * @return
     */
    protected abstract fun createNetErrView(): View?

    /**
     * 加载失败后重试按钮
     */
    private var loadFailedRetryListener: LoadFailedRetryListener? = null

    /**
     *
     */
    var loadEmptyListener: (() -> Unit)? = null

    /**
     * 空视图
     *
     * @return
     */
    protected abstract fun createEmptyView(): View?
    private fun init(context: Context) {
        this.mContext = context
        setOnClickListener { }
    }

    /**
     * 设置正常内容
     *
     * @param contentView
     */
    fun setContentView(contentView: View?) {
        mSuccessView = contentView
        mSuccessView?.let {
            addView(
                it, 0, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            layoutState = State.LOADING
        }
    }

    fun setContentView(contentView: Int) {
        setContentView(LayoutInflater.from(context).inflate(contentView, this))
    }

    /**
     * 加载中
     */
    private fun onLoading() {
        removeViewByState(intArrayOf(State.LOADING, State.NO_NETWORK, State.EMPTY, State.FAILED))
        if (mLoadingView == null) {
            mLoadingView = createLoadingView()
        }

        mLoadingView?.let {
            addView(
                it, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    /**
     * 点击重试
     */
    fun retry() {
        loadFailedRetryListener?.onLoadFailed()
    }


    /**
     * 加载失败
     */
    private fun onLoadFailed() {
        if (!NetworkUtils.isConnected()) {
            onNetWorkErr()
        } else {

            removeViewByState(
                intArrayOf(
                    State.LOADING,
                    State.NO_NETWORK,
                    State.EMPTY,
                    State.FAILED
                )
            )
            if (mFailedView == null) {
                mFailedView = createLoadFailedView()
            }

            mFailedView?.let {
                addView(
                    it, LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    }

    /**
     * 加载失败
     */
    private fun onNetWorkErr() {
        removeViewByState(intArrayOf(State.LOADING, State.NO_NETWORK, State.EMPTY, State.FAILED))
        if (mNetErrView == null) {
            mNetErrView = createNetErrView()
        }

        mNetErrView?.let {
            addView(
                mNetErrView, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    /**
     * 加载空视图
     */
    private fun onLoadEmpty() {
        removeViewByState(intArrayOf(State.LOADING, State.NO_NETWORK, State.EMPTY, State.FAILED))

        if (mEmptyView == null) {
            createNetErrView()
        }

        mEmptyView?.let {
            addView(
                it, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }


    fun setLoadFailedRetryListener(loadFailedRetryListener: LoadFailedRetryListener?) {
        this.loadFailedRetryListener = loadFailedRetryListener
    }

    /**
     * 加载成功
     */
    private fun onLoadSuccess() {
        removeViewByState(intArrayOf(State.LOADING, State.NO_NETWORK, State.EMPTY, State.FAILED))
    }


    private fun removeViewByState(state: IntArray? = null) {

        state?.forEach { loadState ->
            when (loadState) {
                State.LOADING -> {
                    mLoadingView?.let { removeView(it) }
                }
                State.FAILED -> {
                    mFailedView?.let { removeView(it) }
                }
                State.EMPTY -> {
                    mEmptyView?.let { removeView(it) }
                }
                State.NO_NETWORK -> {
                    mNetErrView?.let { removeView(it) }
                }
            }
        }
    }

    @set:Synchronized
    var layoutState: Int
        get() = state
        set(state) {
            this.state = state
            when (this.state) {
                State.LOADING -> onLoading()
                State.FAILED, State.NO_NETWORK -> onLoadFailed()
                State.SUCCESS -> onLoadSuccess()
                State.EMPTY -> onLoadEmpty()
                else -> {
                }
            }
        }

    init {
        init(context)
    }
}