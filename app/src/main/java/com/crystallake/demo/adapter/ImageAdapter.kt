package com.crystallake.demo.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.crystallake.base.fastrecycler.adapter.SingleDataBindingAdapter
import com.crystallake.demo.MainActivityViewModel
import com.crystallake.demo.SecondActivity
import com.crystallake.demo.databinding.ImageItemBinding
import java.util.concurrent.atomic.AtomicBoolean

class ImageAdapter(private val fragment: Fragment, private val itemClick: ((Int) -> Unit)? = null) :
    SingleDataBindingAdapter<Int, ImageItemBinding>() {
    private val enterTransitionStarted: AtomicBoolean by lazy {
        AtomicBoolean()
    }

    override fun generateDataBinding(inflater: LayoutInflater, parent: ViewGroup): ImageItemBinding {
        return ImageItemBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(binding: ImageItemBinding, position: Int) {
        binding.image.setImageResource(dataList[position])
        if (MainActivityViewModel.currentPosition == position && !enterTransitionStarted.getAndSet(true)) {
            fragment.startPostponedEnterTransition()
        }
        binding.image.transitionName = "image_view${dataList[position]}"
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {

                val intent = Intent(fragment.context, SecondActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("DrawableRes", dataList[position])
                intent.putExtras(bundle)
                val imagePair = androidx.core.util.Pair.create(binding.image as? View, "profileImage")
//                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.requireActivity(),imagePair)
                val view = binding.image as? View
                view?.let {
                    val options = ActivityOptionsCompat.makeScaleUpAnimation(it, it.width, it.height, 0, 0)
                    fragment.requireActivity().startActivity(intent, options.toBundle())
                }


//                MainActivityViewModel.currentPosition = position
//                view?.let {
//                    (fragment.exitTransition as? TransitionSet)?.excludeTarget(it, true)
//                    val transitioningView = binding.image
//                    fragment.fragmentManager
//                        ?.beginTransaction()
//                        ?.setReorderingAllowed(true)
//                        ?.addSharedElement(transitioningView, transitioningView.transitionName)
//                        ?.replace(R.id.fragment_container, ImagePagerFragment.newInstance(), ImagePagerFragment::class.simpleName)
//                        ?.addToBackStack(null)
//                        ?.commit()
//                }
            }
        }
        binding.cardView.setOnClickListener {
            onItemClickListener?.onItemClick(it, position)
            itemClick?.invoke(position)
        }
    }
}