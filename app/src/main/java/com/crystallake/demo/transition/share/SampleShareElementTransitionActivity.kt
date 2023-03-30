package com.crystallake.demo.transition.share

import android.app.ActivityOptions
import android.content.Intent
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.ActivitySampleShareElementTransitionBinding

class SampleShareElementTransitionActivity : DataBindingActivity<ActivitySampleShareElementTransitionBinding, BaseViewModel>() {
    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_sample_share_element_transition)
    }

    override fun initData() {
        super.initData()
        mBinding?.buttonNextActivity?.setOnClickListener {
            val intent = Intent(this, NextShareElementTransitionActivity::class.java)
            val compat = ActivityOptions.makeSceneTransitionAnimation(
                this,
                mBinding?.textShareElement,
                mBinding?.textShareElement?.transitionName
            )
            startActivity(intent, compat.toBundle())
        }
    }
}