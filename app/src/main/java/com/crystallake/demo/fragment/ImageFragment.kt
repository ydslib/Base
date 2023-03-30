package com.crystallake.demo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.FragmentImageBinding

class ImageFragment : DataBindingFragment<FragmentImageBinding, BaseViewModel>() {
    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun initView(savedInstanceState: Bundle?) {
        val res = arguments?.getInt("DrawableRes") ?: 0
        if (res != 0) {
            mBinding?.imageView?.transitionName = "image_view$res"
            mBinding?.imageView?.setImageResource(res)

            // The postponeEnterTransition is called on the parent ImagePagerFragment, so the
            // startPostponedEnterTransition() should also be called on it to get the transition
            // going in case of a failure.
            parentFragment?.startPostponedEnterTransition()
        }
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image)
    }

    companion object {
        fun newInstance(res: Int): ImageFragment {
            val imageFragment = ImageFragment()
            val bundle = Bundle()
            bundle.putInt("DrawableRes", res)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }

}