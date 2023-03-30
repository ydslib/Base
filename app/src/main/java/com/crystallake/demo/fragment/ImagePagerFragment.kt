package com.crystallake.demo.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.base.fragment.DataBindingFragment
import com.crystallake.base.vm.BaseViewModel
import com.crystallake.demo.MainActivityViewModel
import com.crystallake.demo.R
import com.crystallake.demo.adapter.ImageStatePagerAdapter
import com.crystallake.demo.databinding.FragmentImagePagerBinding

class ImagePagerFragment : DataBindingFragment<FragmentImagePagerBinding, BaseViewModel>() {

    val mainActivityViewModel by lazy {
        getActivityScopeViewModel(MainActivityViewModel::class.java)
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBinding?.viewPager?.let {
            it.adapter =
                ImageStatePagerAdapter(mainActivityViewModel?.dataListLiveData?.value ?: mutableListOf(), childFragmentManager)
            it.currentItem = MainActivityViewModel.currentPosition
            it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    MainActivityViewModel.currentPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        prepareSharedElementTransition()

        if (savedInstanceState == null){
            postponeEnterTransition()
        }
        return view
    }

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_image_pager)
    }

    companion object {
        fun newInstance(): ImagePagerFragment {
            val imagePagerFragment = ImagePagerFragment()

            return imagePagerFragment
        }
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                mBinding?.viewPager?.let {
                    val currentFragment =
                        it.adapter?.instantiateItem(it, MainActivityViewModel.currentPosition) as? Fragment
                    currentFragment?.view?.let { view->
                        sharedElements?.put(names!![0],view.findViewById(R.id.image_view))
                    }
                }
            }
        })
    }
}