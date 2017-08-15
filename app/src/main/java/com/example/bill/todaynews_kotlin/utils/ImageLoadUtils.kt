package com.example.bill.todaynews_kotlin.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bill.todaynews_kotlin.R
import java.lang.Exception

/**
 * Created by Bill on 2017/8/14.
 */
class ImageLoadUtils {
    companion object {
        fun display(context: Context, imageView: ImageView?, url: String) {
            if (imageView == null) {
                throw IllegalArgumentException("imageView must be assigned")
            }
            Glide.with(context).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_loading)
                    .error(R.drawable.ic_empty_picture)
                    .crossFade()
                    .into(imageView)
        }

        /**
         * 启动页
         */
        fun splashDisplay(activity: Activity, imageView: ImageView?, url: String, listener: ImageLoadListener) {
            Glide.with(activity).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .crossFade()
                    .listener(object : RequestListener<String, GlideDrawable> {
                        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                            listener.onSuccess()
                            return false
                        }

                        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
                            listener.onFail()
                            return false
                        }
                    })
                    .into(imageView)
        }
    }

    interface ImageLoadListener {
        fun onSuccess()

        fun onFail()
    }
}