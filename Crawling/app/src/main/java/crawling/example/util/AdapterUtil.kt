package crawling.example.util

import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import crawling.example.R

@BindingAdapter("loadUrl")
@MainThread
fun ImageView.loadUrl(url: String?) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_launcher_foreground)
        .fitCenter()
        .into(this)
}