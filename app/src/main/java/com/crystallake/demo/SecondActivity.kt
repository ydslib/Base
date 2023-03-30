package com.crystallake.demo

import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.Window
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.databinding.ActivitySecondBinding

class SecondActivity : DataBindingActivity<ActivitySecondBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_second)
    }

    override fun initData() {
        super.initData()
        intent.extras?.let {
            mBinding?.fullPic?.setImageResource(it.getInt("DrawableRes"))
        }
        mBinding?.fullPic?.viewTreeObserver?.addOnPreDrawListener(object :ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                mBinding?.fullPic?.viewTreeObserver?.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
        postponeEnterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.sharedElementsUseOverlay = true
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }


}