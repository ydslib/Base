package com.crystallake.demo.transition.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crystallake.base.fastrecycler.ItemProxy
import com.crystallake.base.fastrecycler.viewholder.ItemViewHolder
import com.crystallake.demo.databinding.ItemCardAlbumImageBinding

class CardItem(private val resourceId: Int, private val onItemClick: ((view: View, position: Int) -> Unit)? = null) :
    ItemProxy<ItemCardAlbumImageBinding>() {
    override fun generateItemViewBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemCardAlbumImageBinding {
        return ItemCardAlbumImageBinding.inflate(inflater, parent, false)
    }

    override fun onBindItemViewHolder(holder: ItemViewHolder, position: Int, binding: ItemCardAlbumImageBinding) {
        binding.mainCardAlbumImage.setOnClickListener {
            onItemClick?.invoke(it, position)
        }

        binding.mainCardAlbumImage.setImageResource(resourceId)
        binding.mainCardAlbumImage.transitionName = "name$resourceId"
        binding.mainCardAlbumImage.tag = "name$resourceId"
    }
}