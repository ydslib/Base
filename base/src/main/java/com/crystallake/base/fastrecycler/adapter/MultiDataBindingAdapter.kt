package com.crystallake.base.fastrecycler.adapter

import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import com.crystallake.base.BuildConfig
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.base.utils.Util

class MultiDataBindingAdapter : BaseAdapter<ItemProxy<*>, ItemViewHolder>() {

    private val mViewType: SparseArray<ItemProxy<*>> by lazy { SparseArray() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemProxy = mViewType[viewType]
        return itemProxy.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (Util.isDebuggable() || isDebuggable()) {
            val startTime = System.nanoTime()
            processViewHolder(holder, position)
            Log.i("onBindViewHolder", "${System.nanoTime() - startTime}")
        } else {
            processViewHolder(holder, position)
        }
    }

    private fun processViewHolder(holder: ItemViewHolder, position: Int) {
        val itemProxy = if (dataList.size > position) {
            dataList[position]
        } else throw ExceptionInInitializerError("please init this viewType from item")

        itemProxy.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemViewType
    }

    fun setData(itemList: List<ItemProxy<*>>?) {
        if (itemList == null) {
            return
        }
        dataList.clear()
        mViewType.clear()
        dataList.addAll(itemList)
        itemList.forEach {
            mViewType.put(it.itemViewType, it)
        }
    }

    fun addData(itemList: List<ItemProxy<*>>?) {
        if (itemList == null) {
            return
        }
        dataList.addAll(itemList)
        itemList.forEach {
            mViewType.put(it.itemViewType, it)
        }
    }

    fun addItem(item: ItemProxy<*>?) {
        if (item == null) {
            return
        }
        dataList.add(item)
        mViewType.put(item.itemViewType, item)
    }

    fun clear() {
        dataList.clear()
        mViewType.clear()
    }

    fun getItem(position: Int): ItemProxy<*> {
        return dataList[position]
    }

}