package com.crystallake.demo.transition.share

import android.transition.Transition
import android.view.ViewAnimationUtils
import androidx.core.view.isVisible
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.ActivityNextShareElementTransitionBinding
import kotlin.math.max

class NextShareElementTransitionActivity : DataBindingActivity<ActivityNextShareElementTransitionBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_next_share_element_transition)
    }

    override fun initData() {
        super.initData()
        initShareTransition()
    }

    private fun initShareTransition() {
        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition?) {
                mBinding?.textInfo?.isVisible = false
            }

            override fun onTransitionEnd(transition: Transition?) {
                mBinding?.textInfo?.isVisible = true
                mBinding?.viewBackground?.isVisible = true
                val width = mBinding?.textInfo?.width ?: 0
                val height = mBinding?.textInfo?.height ?: 0
                val animationBottom = ViewAnimationUtils.createCircularReveal(
                    mBinding?.textInfo,
                    width / 2,
                    height / 2,
                    0f,
                    max(width / 2, height / 2).toFloat()
                )
                animationBottom.duration = 500
                animationBottom.start()

//                mBinding?.viewBackground?.animate()?.alpha(1f)?.setDuration(500)?.start()
                window.sharedElementEnterTransition.removeListener(this)
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionResume(transition: Transition?) {
            }

        })
    }
}