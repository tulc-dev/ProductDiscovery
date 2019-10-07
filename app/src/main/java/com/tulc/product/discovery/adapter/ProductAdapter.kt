package com.tulc.product.discovery.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tulc.product.discovery.R
import com.tulc.product.discovery.paging.PagingState
import com.tulc.product.discovery.databinding.ListItemFooterBinding
import com.tulc.product.discovery.databinding.ListItemProductBinding
import com.tulc.product.discovery.extensions.hideSoftKeyBoard
import com.tulc.product.discovery.models.Product


class ProductAdapter(private var retryClickListener: View.OnClickListener) :
    PagedListAdapter<Product, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private var pagingState = PagingState.LOADING_INITIAL
    private var errorCode: Int? = null

    fun setPagingState(state: PagingState) {
        val previousState = this.pagingState
        val hadExtraRow = hasExtraRow()
        this.pagingState = state
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != state) {
            notifyItemChanged(itemCount - 1)
        }
    }

    fun setErrorCode(errorCode: Int) {
        this.errorCode = errorCode
        if (hasExtraRow()) {
            notifyItemChanged(itemCount - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            FOOTER_VIEW_TYPE -> {
                val binding =
                    ListItemFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                FooterViewHolder(binding)
            }
            PRODUCT_VIEW_TYPE -> {
                val binding =
                    ListItemProductBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ProductViewHolder(binding)
            }
            else -> throw Throwable("Don't support viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ProductViewHolder)?.let {
            getItem(position)?.let {
                holder.bind(it)
            }
        }

        (holder as? FooterViewHolder)?.bind(pagingState, errorCode, retryClickListener)
    }

    private fun hasExtraRow() =
        pagingState == PagingState.LOADING || pagingState == PagingState.FAILED || pagingState == PagingState.LOADED

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == super.getItemCount()) {
            FOOTER_VIEW_TYPE
        } else {
            PRODUCT_VIEW_TYPE
        }
    }


    class ProductViewHolder(private val binding: ListItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener { view ->
                binding.product?.let {
                    view.context.hideSoftKeyBoard()
                    view.findNavController()
                        .navigate(
                            R.id.action_productListingFragment_to_productDetailFragment,
                            bundleOf("product" to it)
                        )
                }
            }
        }

        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    class FooterViewHolder(private val binding: ListItemFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: PagingState, errorCode: Int?, listener: View.OnClickListener?) {
            binding.setRetryClickListener {
                listener?.onClick(it)
            }
            binding.state = state
            binding.errorCode = errorCode
            binding.executePendingBindings()
        }
    }

    companion object {
        private const val PRODUCT_VIEW_TYPE = 0
        private const val FOOTER_VIEW_TYPE = 1

        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(
                oldProduct: Product,
                newProduct: Product
            ) = oldProduct.sku == newProduct.sku

            override fun areContentsTheSame(
                oldProduct: Product,
                newProduct: Product
            ) = oldProduct == newProduct
        }
    }
}
