package com.crystallake.demo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.crystallake.demo.fragment.ImageFragment

class ImageStatePagerAdapter(val dataList: MutableList<Int>, val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(dataList[position])
    }
}