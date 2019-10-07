package com.tulc.product.discovery.utils

import android.content.Context
import com.tulc.product.discovery.extensions.getStatusBarHeight


fun getStatusBarHeight(context: Context): Int {
    return context.getStatusBarHeight()
}
