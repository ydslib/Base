package com.crystallake.demo.transition.share

import android.content.Intent
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.ActivityShareElementTransitionMainBinding

class ShareElementTransitionMainActivity : DataBindingActivity<ActivityShareElementTransitionMainBinding, BaseViewModel>() {

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_share_element_transition_main)
    }

    override fun initData() {
        super.initData()

        mBinding?.layoutActivityShareElement?.setOnClickListener {
            startActivity(Intent(this, SampleShareElementTransitionActivity::class.java))
        }

        mBinding?.layoutShareElementBrowse?.setOnClickListener {
//            startActivity(Intent(this,))
        }
    }
}