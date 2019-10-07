package com.tulc.product.discovery.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tulc.product.discovery.R

import com.tulc.product.discovery.adapter.ProductAdapter
import com.tulc.product.discovery.adapter.RecentSearchAdapter
import com.tulc.product.discovery.data.AppRoomDatabase
import com.tulc.product.discovery.data.RecentSearchRepository
import com.tulc.product.discovery.paging.PagingState
import com.tulc.product.discovery.databinding.FragmentProductListingBinding
import com.tulc.product.discovery.extensions.hideSoftKeyBoard
import com.tulc.product.discovery.models.RecentSearch
import com.tulc.product.discovery.utils.TAG
import com.tulc.product.discovery.viewmodel.ProductListingViewModel
import com.tulc.product.discovery.viewmodel.ProductListingViewModelFactory


class ProductListingFragment : Fragment() {

    private var binding: FragmentProductListingBinding? = null
    private val productListingViewModel by viewModels<ProductListingViewModel> {
        val repository = RecentSearchRepository(
            AppRoomDatabase.getDatabase(requireContext()).recentSearchDao()
        )
        ProductListingViewModelFactory(repository)
    }
    private val productAdapter: ProductAdapter
    private val recentSearchAdapter: RecentSearchAdapter

    init {
        productAdapter = ProductAdapter(View.OnClickListener {
            productListingViewModel.invokeRetry()
        })
        recentSearchAdapter = RecentSearchAdapter(object :
            RecentSearchAdapter.OnRecentSearchSelectedListener {
            override fun onSelect(recentSearch: RecentSearch) {
                productListingViewModel.refreshing.value = false
                productListingViewModel.search(context, recentSearch.text)
            }

            override fun onDelete(recentSearch: RecentSearch) {
                productListingViewModel.refreshing.value = false
                productListingViewModel.deleteRecentSearch(recentSearch)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentProductListingBinding.inflate(inflater, container, false).apply {
                viewModel = productListingViewModel

                productRecyclerView.adapter = productAdapter
                productRecyclerView.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_MOVE) {
                        activity?.hideSoftKeyBoard()
                    }
                    return@setOnTouchListener false
                }

                recentSearchRecyclerView.adapter = recentSearchAdapter
                recentSearchRecyclerView.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_MOVE) {
                        activity?.hideSoftKeyBoard()
                    }
                    return@setOnTouchListener false
                }

                searchEditText.apply {
                    setOnTouchListener { _, event ->
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            productListingViewModel.startTyping()
                        }
                        return@setOnTouchListener false
                    }

                    setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            productListingViewModel.refreshing.value = false
                            productListingViewModel.search(context, true)
                            return@setOnEditorActionListener true
                        }
                        return@setOnEditorActionListener false
                    }
                }

                refreshLayout.setOnRefreshListener {
                    productListingViewModel.refreshing.value = true
                    productListingViewModel.search(context, true)
                }
            }
        }

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.window?.decorView?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        binding?.lifecycleOwner = viewLifecycleOwner

        productListingViewModel.apply {
            products.observe(this@ProductListingFragment, Observer {
                productAdapter.submitList(it)
            })

            pagingState.observe(this@ProductListingFragment, Observer {
                Log.d(TAG, "loading new state $it")
                productAdapter.setPagingState(it)
                if (it == PagingState.LOADING_INITIAL) {
                    (binding?.productRecyclerView?.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                        0,
                        0
                    )
                } else {
                    productListingViewModel.refreshing.value = false
                }
            })

            errorCode.observe(this@ProductListingFragment, Observer {
                productAdapter.setErrorCode(it)
            })

            recentSearches.observe(this@ProductListingFragment, Observer {
                Log.d(TAG, "Recent Search List Size ${it.size}")
                recentSearchAdapter.submitList(it)
            })
        }
    }

    fun onBackPressed(): Boolean {
        view?.let {
            if (productListingViewModel.typing.value == true) {
                productListingViewModel.onClearQuery(it)
                return true
            }
        }

        return false
    }
}
