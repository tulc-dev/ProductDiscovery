package com.tulc.product.discovery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulc.product.discovery.databinding.ListItemRecentSearchBinding
import com.tulc.product.discovery.models.RecentSearch


class RecentSearchAdapter(private val recentSearchListener: OnRecentSearchSelectedListener) :
    RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {

    private val recentSearches = ArrayList<RecentSearch>()

    fun submitList(recentSearches: List<RecentSearch>) {
        this.recentSearches.clear()
        this.recentSearches.addAll(recentSearches)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val binding =
            ListItemRecentSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RecentSearchViewHolder(binding)
    }

    override fun getItemCount(): Int = recentSearches.size

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        holder.bind(recentSearches[position], recentSearchListener)
    }

    class RecentSearchViewHolder(private val binding: ListItemRecentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recentSearch: RecentSearch, listener: OnRecentSearchSelectedListener?) {
            binding.recentSearchListener = listener
            binding.recentSearch = recentSearch
            binding.executePendingBindings()
        }
    }

    interface OnRecentSearchSelectedListener {
        fun onSelect(recentSearch: RecentSearch)
        fun onDelete(recentSearch: RecentSearch)
    }
}
