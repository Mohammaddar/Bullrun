package com.example.bullrun

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

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
        .into(view)


}

@BindingAdapter("imageBitmap", "context")
fun imageBitmap(view: ImageView, bitmap:Bitmap, context: Context) {
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

    Glide.with(context).load(bitmap)
        .circleCrop()
        .into(view)


}

@BindingAdapter("priceChangePercentage","context")
fun setPriceChangeColor(view: MaterialCardView,changePercentage: Double,context: Context) {
    if (changePercentage>=0.0){
        view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gr))
    }else{
        view.setCardBackgroundColor(ContextCompat.getColor(context, R.color.re))
    }
}

suspend fun imageUrl2(
    view: ImageView,
    url: String?,
    context: Context,
    coinName: String,
    card: MaterialCardView
) {

    var r = 0
    var g = 0
    var b = 0
    Log.d("TAGH", "${Thread.currentThread().name}   1")
    val def = CoroutineScope(Dispatchers.Main).async {
        Log.d("TAGH", "${Thread.currentThread().name}   2")
        Glide.with(context).load(url)
            .circleCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("TAGH", "${Thread.currentThread().name}   3")
                    val bitmap = resource!!.toBitmap()
                    val width = bitmap.width
                    val height = bitmap.height
                    var pixel = 0
                    for (i in 1..5) {
                        for (j in 1..5) {
                            Log.d("TAGC", "${coinName} $i $j : ${r / 25}  ${g / 25}  ${b / 25}")
                            pixel = bitmap.getPixel((width / 6) * i, (height / 6) * j)
                            r += pixel.red
                            g += pixel.green
                            b += pixel.blue
                        }
                    }
                    setColor(r / 25, g / 25, b / 25, view, card, context)
                    Log.d("TAGH", "${Thread.currentThread().name}   4")
                    Log.d("TAGC", "${coinName} final : ${r / 25}  ${g / 25}  ${b / 25}")
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

            })
            .into(view)
        Log.d("TAGH", "${Thread.currentThread().name}   5")
    }
    Log.d("TAGH", "${Thread.currentThread().name}   6")
    def.await()
    Log.d("TAGH", "${Thread.currentThread().name}   7")
}

fun setColor(r: Int, g: Int, b: Int, view: ImageView, card: MaterialCardView, context: Context) {

    val colors = listOf<Triple<Int, Int, Int>>(
        Triple(222, 226, 230),
        Triple(183, 228, 199),
        Triple(255, 215, 186),
        Triple(253, 255, 182),
        Triple(202, 240, 248)
    )

    var minDiff = 2000
    var index = 0
    for (i in colors.indices) {
        val diff = kotlin.math.abs(r - colors[i].toList()[0]) +
                kotlin.math.abs(g - colors[i].toList()[1]) +
                kotlin.math.abs(b - colors[i].toList()[2])
        Log.d("TAGD", "$diff")
        if (diff < minDiff) {
            minDiff = diff
            index = i
        }
    }
    when (index) {
        0 -> card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_gray))
        1 -> card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_green))
        2 -> card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_orange))
        3 -> card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_yellow))
        4 -> card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_blue))
    }

}
