package com.firebaseauth.core

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.firebaseauth.R
import java.io.IOException

class GlideLoader(val context: Context) {


    fun loadUserPicture(image: Any, imageView: ImageView) {
        try{
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_background) // A default place holder if image is failed to load
                .into(imageView) // The view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}