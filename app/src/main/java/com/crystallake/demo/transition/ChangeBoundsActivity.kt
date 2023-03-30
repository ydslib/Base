package com.crystallake.demo.transition

import android.content.Context
import android.content.Intent
import android.transition.ChangeBounds
import android.transition.Scene
import android.transition.TransitionManager
import android.transition.TransitionSet
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.ActivityChangeBoundsBinding

/**
 * https://blog.csdn.net/whoami_I/article/details/103734044
 */
class ChangeBoundsActivity : DataBindingActivity<ActivityChangeBoundsBinding, BaseViewModel>() {


    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, ChangeBoundsActivity::class.java)
            context.startActivity(intent)
        }
    }

    var scene01: Scene? = null
    var scene02: Scene? = null
    var toogle = false

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_change_bounds)
    }

    override fun initData() {
        super.initData()
        afterSetContentView()

        mBinding?.changeBoundsBtn?.setOnClickListener {
            clickStartScene()
        }
    }

    private fun clickStartScene() {
        val transitionSet = TransitionSet()
        val changeBounds = ChangeBounds()
        transitionSet.duration = 3000
        transitionSet.addTransition(changeBounds)
        TransitionManager.go((if (toogle) scene02 else scene01), transitionSet)
        toogle = !toogle
    }

    private fun afterSetContentView() {
        scene01 = Scene.getSceneForLayout(mBinding?.sceneRoot, R.layout.change_bounds_sence_01, this)
        scene02 = Scene.getSceneForLayout(mBinding?.sceneRoot, R.layout.change_bounds_sence_02, this)
        TransitionManager.go(scene01)
        toogle = true
    }
}