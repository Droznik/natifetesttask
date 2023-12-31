package com.example.giphyfornatife.ui.main

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View.GONE
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener

import com.bumptech.glide.request.target.Target
import com.example.giphyfornatife.data.model.GifObject
import com.example.giphyfornatife.databinding.GifItemBinding
import com.example.giphyfornatife.ui.viewpager.ViewPagerActivity

class GifViewHolder(private val binding: GifItemBinding, private val getGifUrls: () -> ArrayList<String>): RecyclerView.ViewHolder(binding.root){

    fun bind(gif: GifObject, position: Int) {
        val itemView = binding.root
        Glide.with(itemView.context)
            .load(gif.images.downsampledImage.url)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.gifProgress.visibility = GONE
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                    binding.gifProgress.visibility = GONE
                    return false
                }
            })
            .transition(withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.ivGif)

        itemView.setOnClickListener{openInViewPagerActivity(position)}
    }


    private fun openInViewPagerActivity(position: Int) {
        val intent = Intent(itemView.context, ViewPagerActivity::class.java).apply {
            putExtra("id", position)
            putStringArrayListExtra("gif_urls", getGifUrls())
        }
        itemView.context.startActivity(intent)
    }
}