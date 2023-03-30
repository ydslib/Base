package com.crystallake.demo

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fastrecycler.adapter.MultiDataBindingAdapter
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.databinding.ActivityMainBinding
import com.crystallake.demo.item.SelectItem
import com.crystallake.demo.transition.ChangeBoundsActivity

class MainActivity : DataBindingActivity<ActivityMainBinding, BaseViewModel>(), View.OnClickListener {

    companion object {
        const val CHANGEBOUNDS_TRANSITION = "ChangeBounds动画"
    }

    val dataList by lazy {
        mutableListOf(CHANGEBOUNDS_TRANSITION)
    }

    val adapter by lazy {
        MultiDataBindingAdapter()
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main).addBindingParam(BR.adapter, adapter)
    }

    override fun initData() {
        super.initData()
        dataList.forEach {
            adapter.addItem(SelectItem(it, this))
        }

    }

    override fun onClick(v: View?) {
        when ((v as? Button)?.text) {
            CHANGEBOUNDS_TRANSITION -> {
                ChangeBoundsActivity.startActivity(this)
            }
        }
    }

}