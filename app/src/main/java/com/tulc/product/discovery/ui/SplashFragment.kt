package com.tulc.product.discovery.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.tulc.product.discovery.R
import com.tulc.product.discovery.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private val finished = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSplashBinding.inflate(inflater, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Handler().postDelayed({
            finished.value = true
        }, 1000)

        finished.observe(this, Observer {
            if (it) {
                findNavController().navigate(R.id.action_splashFragment_to_productListingFragment)
            }
        })
    }
}
