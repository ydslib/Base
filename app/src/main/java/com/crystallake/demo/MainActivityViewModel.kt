package com.crystallake.demo

import androidx.lifecycle.MutableLiveData
import com.crystallake.base.vm.BaseViewModel

class MainActivityViewModel : BaseViewModel() {

    companion object{
        const val GRID_FRAGMENT = 0
        const val IMAGE_PAGER_FRAGMENT = 1
        const val IMAGE_FRAGMENT = 2

        var currentPosition = 0
    }
    val dataListLiveData: MutableLiveData<MutableList<Int>> by lazy {
        MutableLiveData<MutableList<Int>>()
    }


    val navigationFragment = MutableLiveData<Int>(GRID_FRAGMENT)

    fun initDataList(){
        val dataList = mutableListOf<Int>()
        dataList.add(R.drawable.animal_2024172)
        dataList.add(R.drawable.beetle_562035)
        dataList.add(R.drawable.bug_189903)
        dataList.add(R.drawable.butterfly_417971)
        dataList.add(R.drawable.butterfly_dolls_363342)
        dataList.add(R.drawable.dragonfly_122787)
        dataList.add(R.drawable.dragonfly_274059)
        dataList.add(R.drawable.dragonfly_689626)
        dataList.add(R.drawable.grasshopper_279532)
        dataList.add(R.drawable.hover_fly_61682)
        dataList.add(R.drawable.hoverfly_546692)
        dataList.add(R.drawable.insect_278083)
        dataList.add(R.drawable.morpho_43483)
        dataList.add(R.drawable.nature_95365)
        dataListLiveData.value = dataList
    }

}