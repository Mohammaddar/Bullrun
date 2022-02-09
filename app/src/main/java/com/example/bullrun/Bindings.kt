package com.example.bullrun

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl", "context")
fun imageUrl(view: ImageView, url: String?, context: Context) {
//    CoroutineScope(Dispatchers.IO).launch {
//        Log.d("TAG", "loading $url")
//        if (!url.isNullOrEmpty()) {
//            val g = Glide.with(context).load(url)
//                .circleCrop()
//                //.placeholder(R.drawable.loading_spinner)
//                    withContext(Dispatchers.Main) {
//                       g.into(view);
//                    }
//        }
//        Log.d("TAG", "done $url")
//    }

    Glide.with(context).load(url)
        .circleCrop()
        .into(view);

}