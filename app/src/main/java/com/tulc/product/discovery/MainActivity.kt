package com.tulc.product.discovery

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.tulc.product.discovery.ui.ProductListingFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
//        }

        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val topFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host)
                ?.childFragmentManager?.fragments?.get(0)
        if ((topFragment as? ProductListingFragment)?.onBackPressed() == true) return

        super.onBackPressed()
    }
}
