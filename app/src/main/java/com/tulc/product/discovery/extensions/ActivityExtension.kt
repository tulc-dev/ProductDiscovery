package com.tulc.product.discovery.extensions

import android.app.Activity
import android.view.inputmethod.InputMethodManager


fun Activity.hideSoftKeyBoard() {
    currentFocus?.apply {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}
