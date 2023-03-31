package com.crystallake.demo.transition.browse

import android.app.SharedElementCallback
import android.content.Intent
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.crystallake.base.activity.DataBindingActivity
import com.crystallake.base.config.DataBindingConfig
import com.crystallake.demo.MainActivityViewModel
import com.crystallake.demo.R
import com.crystallake.demo.databinding.ActivityBrowseBinding
import com.crystallake.demo.transition.browse.BrowseShareElementTransitionActivity.Companion.EXTRA_CURRENT_POSITION
import com.crystallake.demo.transition.browse.BrowseShareElementTransitionActivity.Companion.EXTRA_START_POSITION

class ActivityBrowse : DataBindingActivity<ActivityBrowseBinding, MainActivityViewModel>() {

    val adapter by lazy {
        DetailsFragmentPagerAdapter(
            mViewModel.dataListLiveData.value?.toList() ?: arrayListOf(),
            startPosition ?: 0,
            supportFragmentManager
        )
    }

    var startPosition: Int? = null
    var currentPosition: Int? = null

    var mCurrentDetailsFragment: DetailFragment? = null

    private var mIsReturning = false

    override fun initDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_browse)
    }

    private fun initShareElement() {
        window.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.image_shared_element_transition)
        postponeEnterTransition()
        setEnterSharedElementCallback(mCallback)
    }

    override fun initOtherVM() {
        super.initOtherVM()
        startPosition = intent.extras?.getInt(BrowseShareElementTransitionActivity.EXTRA_START_POSITION)
    }

    override fun initData() {
        super.initData()
        initShareElement()
        mViewModel.initDataList()
        currentPosition = startPosition
        mBinding?.viewPager?.let {
            it.adapter = adapter
            it.currentItem = currentPosition ?: 0
            it.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    currentPosition = position
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }

    }

    inner class DetailsFragmentPagerAdapter(
        val dataList: List<Int>,
        private val startPosition: Int,
        fragmentManager: FragmentManager
    ) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getCount(): Int {
            return dataList.size
        }

        override fun getItem(position: Int): Fragment {
            return DetailFragment.newInstances(position, startPosition = startPosition, dataList[position])
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, fragment: Any) {
            super.setPrimaryItem(container, position, fragment)
            mCurrentDetailsFragment = fragment as? DetailFragment
        }

    }

    override fun finishAfterTransition() {
        mIsReturning = true
        val data = Intent()
        data.putExtra(EXTRA_START_POSITION, startPosition)
        data.putExtra(EXTRA_CURRENT_POSITION, currentPosition)
        setResult(RESULT_OK, data)
        super.finishAfterTransition()
    }

    val mCallback = object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
            if (mIsReturning) {
                val sharedElement = mCurrentDetailsFragment?.getAlbumImage()
                sharedElement?.let {
                    if (currentPosition != startPosition) {
                        names?.clear()
                        sharedElement?.transitionName?.let {
                            names?.add(it)
                        }
                        sharedElements?.clear()
                        sharedElement?.let {
                            sharedElements?.put(it.transitionName, it)
                        }
                    }
                } ?: kotlin.run {
                    names?.clear()
                    sharedElements?.clear()
                }
            }

            super.onMapSharedElements(names, sharedElements)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}