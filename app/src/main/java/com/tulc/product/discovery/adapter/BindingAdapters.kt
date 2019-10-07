package com.tulc.product.discovery.adapter

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tulc.product.discovery.R
import com.tulc.product.discovery.utils.ERROR_CODE_NO_INTERNET_CONNECTION
import com.tulc.product.discovery.utils.ERROR_CODE_TIME_OUT
import com.tulc.product.discovery.utils.ERROR_CODE_UNKNOWN


@BindingAdapter("isGone")
fun View.bindIsGone(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("specifiesText")
fun TextView.setSpecifiesText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml("<s>$text</s>", 0))
    } else {
        setText(Html.fromHtml("<s>$text</s>"))
    }
}

@BindingAdapter("html")
fun TextView.setHtml(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, 0))
    } else {
        setText(Html.fromHtml(text))
    }
}

@BindingAdapter("errorCode")
fun TextView.setErrorCode(errorCode: Int?) {
    when (errorCode) {
        ERROR_CODE_UNKNOWN -> setText(R.string.msg_unknown_error)
        ERROR_CODE_TIME_OUT -> setText(R.string.msg_timeout_error)
        ERROR_CODE_NO_INTERNET_CONNECTION -> setText(R.string.msg_no_internet_connection)
        else -> text = ""
    }
}

@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    setImageResource(R.drawable.ic_placeholder)

    url?.let {
        Glide
            .with(this.context)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(this)
    }
}
