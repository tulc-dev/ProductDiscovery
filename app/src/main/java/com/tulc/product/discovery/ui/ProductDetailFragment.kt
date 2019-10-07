package com.tulc.product.discovery.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs

import com.tulc.product.discovery.adapter.ImageViewPagerAdapter
import com.tulc.product.discovery.adapter.TechnicalDataAdapter
import com.tulc.product.discovery.databinding.FragmentProductDetailBinding
import com.tulc.product.discovery.viewmodel.ProductDetailViewModel
import com.tulc.product.discovery.viewmodel.ProductDetailViewModelFactory
import com.tulc.product.discovery.viewmodel.SharedViewModel
import com.google.android.material.tabs.TabLayout
import com.tulc.product.discovery.R


class ProductDetailFragment : Fragment() {

    private val args by navArgs<ProductDetailFragmentArgs>()
    private var binding: FragmentProductDetailBinding? = null
    private val productDetailViewModel by viewModels<ProductDetailViewModel> {
        val sharedViewModel by activityViewModels<SharedViewModel>()
        ProductDetailViewModelFactory(args.product, sharedViewModel.quantityCart)
    }
    private val imageAdapter = ImageViewPagerAdapter()
    private val technicalDataAdapter = TechnicalDataAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentProductDetailBinding.inflate(inflater, container, false)
                .apply {
                    viewModel = productDetailViewModel
                    imageViewPager.adapter = imageAdapter
                    imageIndicator.setupWithViewPager(imageViewPager)

                    technicalDataRecyclerView.adapter = technicalDataAdapter

                    productContentTabLayout.addOnTabSelectedListener(object :
                        TabLayout.OnTabSelectedListener {
                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {

                        }

                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            productDetailViewModel.setTabSelection(
                                productContentTabLayout.selectedTabPosition
                            )
                        }

                    })
                    productContentTabLayout.getTabAt(TECHNICAL_DATA_TAB_INDEX)?.select()

                    productDetailViewModel.loadDetail()
                }
        }

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding?.lifecycleOwner = viewLifecycleOwner

        productDetailViewModel.apply {
            images.observe(this@ProductDetailFragment, Observer {
                imageAdapter.submitList(it)
            })

            technicals.observe(this@ProductDetailFragment, Observer {
                technicalDataAdapter.submitList(it)
            })

            showMore.observe(this@ProductDetailFragment, Observer {
                technicalDataAdapter.setMaxItemCount(
                    if (it == true) 4 else null
                )
            })
        }
    }

    companion object {
        const val PRODUCT_DESCRIPTION_TAB_INDEX = 0
        const val TECHNICAL_DATA_TAB_INDEX = 1
        const val PRICE_COMPARING_TAB_INDEX = 2
    }
}
