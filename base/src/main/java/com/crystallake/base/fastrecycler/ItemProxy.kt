package com.crystallake.base.fastrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder

abstract class ItemProxy<VB : ViewBinding> {

    val itemViewType: Int get() = this.javaClass.simpleName.hashCode()

    var mBinding: VB? = null

    fun onCreateViewHolder(parent: ViewGroup): ItemViewHolder {
        val binding = generateItemViewBinding(LayoutInflater.from(parent.context), parent)
        return ItemViewHolder(binding)
    }

    fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        (holder.binding as? VB)?.let {
            mBinding = it
            onBindItemViewHolder(holder, position, it)
        }
    }

    abstract fun generateItemViewBinding(inflater: LayoutInflater, parent: ViewGroup?): VB

    abstract fun onBindItemViewHolder(holder: ItemViewHolder, position: Int, binding: VB)
}