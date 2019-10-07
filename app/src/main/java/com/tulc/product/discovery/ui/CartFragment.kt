package com.tulc.product.discovery.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.tulc.product.discovery.databinding.FragmentCartBinding
import com.tulc.product.discovery.viewmodel.CartViewModel
import com.tulc.product.discovery.viewmodel.CartViewModelFactory
import com.tulc.product.discovery.viewmodel.SharedViewModel


class CartFragment : Fragment() {

    private var binding: FragmentCartBinding? = null
    private val cartViewModel by viewModels<CartViewModel> {
        val sharedViewModel by activityViewModels<SharedViewModel>()
        CartViewModelFactory(sharedViewModel.quantityCart)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentCartBinding.inflate(inflater, container, false).apply {
                viewModel = cartViewModel
            }
        }
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding?.lifecycleOwner = viewLifecycleOwner
    }
}
