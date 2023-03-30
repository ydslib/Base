package com.crystallake.demo.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.demo.databinding.SelectItemBinding

class SelectItem(private val text: String, private val listener: View.OnClickListener? = null) : ItemProxy<SelectItemBinding>() {
    override fun generateItemViewBinding(inflater: LayoutInflater, parent: ViewGroup?): SelectItemBinding {
        return SelectItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(holder: ItemViewHolder, position: Int, binding: SelectItemBinding) {
        binding.btn.text = text

        binding.btn.setOnClickListener {
            listener?.onClick(it)
        }
    }
}