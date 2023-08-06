package com.mohammad.journalapp

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp


data class Journal(
    val title:String,
    val thoughts: String,
    val imageUrl: String,
    val userId:String,
    val timeAdded: Timestamp,
    var username: String

){
    // Binding Adapter
    // images to display into imageviews in custom views
    object DataBindingAdapter{
        @BindingAdapter("imageUrl")
        @JvmStatic
        fun setImageByRes(imageView: ImageView,imageUrl: String){
            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)

        }
    }
}
