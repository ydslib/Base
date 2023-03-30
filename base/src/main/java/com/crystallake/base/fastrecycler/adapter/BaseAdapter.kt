package com.crystallake.base.fastrecycler.adapter

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    val dataList: MutableList<T> by lazy {
        mutableListOf()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

//    fun setData(list: List<T>?) {
//        if (list.isNullOrEmpty()) {
//            return
//        }
//        dataList.clear()
//        clearViewType()
//        dataList.addAll(list)
//    }
//
//    fun addItem(itemView: T) {
//        dataList.add(itemView)
//        addViewType(itemView)
//    }
//
//    fun removeItem(itemView: T) {
//        if (dataList.contains(itemView)) {
//            dataList.remove(itemView)
//        }
//        removeViewType(itemView)
//    }
//
//    open fun addViewType(itemView: T) {
//
//    }
//
//    open fun removeViewType(itemView: T) {
//
//    }
//
//    open fun clearViewType() {
//
//    }
//
//    open fun addAllViewType(){
//
//    }
}