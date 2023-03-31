package com.crystallake.demo.transition.browse

import android.os.Bundle
import android.transition.Transition
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.core.view.isVisible
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.FragmentDetailBinding
import kotlin.math.hypot

class DetailFragment : DataBindingFragment<FragmentDetailBinding, BaseViewModel>() {

    var mCurrentPosition: Int? = null
    var mStartPosition: Int? = null
    var mRes: Int? = null

    companion object {
        fun newInstances(currentPosition: Int, startPosition: Int, res: Int): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putInt(BrowseShareElementTransitionActivity.EXTRA_CURRENT_POSITION, currentPosition)
            args.putInt(BrowseShareElementTransitionActivity.EXTRA_START_POSITION, startPosition)
            args.putInt("DrawableRes", res)
            fragment.arguments = args
            return fragment
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mCurrentPosition = arguments?.getInt(BrowseShareElementTransitionActivity.EXTRA_CURRENT_POSITION)
        mStartPosition = arguments?.getInt(BrowseShareElementTransitionActivity.EXTRA_START_POSITION)
        mRes = arguments?.getInt("DrawableRes")

        val viewBackground = mBinding?.viewBackground
        val mAlbumImage = mBinding?.imageDetailPicture
        val textTitle = mBinding?.textDetailTitle
        textTitle?.text = "测试"
        mAlbumImage?.transitionName = "name:$mRes"
        mRes?.let {
            mAlbumImage?.setImageResource(it)
        }
        if (mStartPosition == mCurrentPosition) {
            requireActivity().window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition?) {
                    viewBackground?.isVisible = false
                }

                override fun onTransitionEnd(transition: Transition?) {
                    viewBackground?.isVisible = true
                    val height = viewBackground?.height ?: 0
                    val width = viewBackground?.width ?: 0
                    val animationBottom = ViewAnimationUtils.createCircularReveal(
                        viewBackground,
                        width/2,
                        height/2,
                        0f,
                        hypot(width.toDouble(), height.toDouble()).toFloat()
                    )
                    animationBottom.duration = 1200
                    animationBottom.start()
                    requireActivity().window.sharedElementEnterTransition.removeListener(this)
                }

                override fun onTransitionCancel(transition: Transition?) {

                }

                override fun onTransitionPause(transition: Transition?) {

                }

                override fun onTransitionResume(transition: Transition?) {

                }

            })
        }
        startPostponedEnterTransitionV()

    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_detail)
    }

    fun startPostponedEnterTransitionV() {
        if (mCurrentPosition == mStartPosition) {
            mBinding?.imageDetailPicture?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    mBinding?.imageDetailPicture?.viewTreeObserver?.removeOnPreDrawListener(this)
                    requireActivity().startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    fun getAlbumImage(): ImageView? {
        return mBinding?.imageDetailPicture
    }
}