package com.crystallake.base.fastrecycler.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.crystallake.base.BuildConfig
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.base.utils.Util

abstract class SingleDataBindingAdapter<T, VB : ViewBinding> : BaseAdapter<T, ItemViewHolder>() {

    var mBinding: VB? = null

    var onItemClickListener: OnItemClickListener? = null

    var mItemView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = generateDataBinding(LayoutInflater.from(parent.context), parent)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (Util.isDebuggable() || isDebuggable()) {
            val startTime = System.nanoTime()
            processBindViewHolder(holder, position)
            Log.i("onBindViewHolder", "cost: ${System.nanoTime() - startTime}")
        } else {
            processBindViewHolder(holder, position)
        }
    }

    private fun processBindViewHolder(holder: ItemViewHolder, position: Int) {
        (holder.binding as? VB)?.let {
            mBinding = it
            onItemClickListener?.let { itemClickListener ->
                mBinding?.root?.setOnClickListener { itemClickListener.onItemClick(mBinding?.root, position) }
            }
            mItemView = holder.itemView
            onBindViewHolder(it, position)
        }
    }

    fun getItemView(): View? {
        return mItemView
    }

    fun setData(list: List<T>?) {
        if (list == null) {
            return
        }
        dataList.addAll(list)
    }

    fun addData(list: List<T>?) {
        if (list == null) {
            return
        }
        dataList.addAll(list)
    }

    abstract fun generateDataBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    abstract fun onBindViewHolder(binding: VB, position: Int)

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }
}