package com.tulc.product.discovery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulc.product.discovery.databinding.ListItemTechnicalBinding
import com.tulc.product.discovery.models.Attribute
import kotlin.math.min


class TechnicalDataAdapter :
    RecyclerView.Adapter<TechnicalDataAdapter.TechnicalViewHolder>() {

    private val technicals = ArrayList<Attribute>()
    private var maxItemCount: Int? = null

    fun submitList(technicals: List<Attribute>) {
        this.technicals.clear()
        this.technicals.addAll(technicals)
        notifyDataSetChanged()
    }

    fun setMaxItemCount(maxItemCount: Int?) {
        this.maxItemCount = maxItemCount
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechnicalViewHolder {
        val binding =
            ListItemTechnicalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TechnicalViewHolder(binding)
    }

    override fun getItemCount(): Int =
        if (maxItemCount == null) technicals.size else min(maxItemCount!!, technicals.size)

    override fun onBindViewHolder(holder: TechnicalViewHolder, position: Int) {
        holder.bind(position, technicals[position])
    }

    class TechnicalViewHolder(private val binding: ListItemTechnicalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, technical: Attribute) {
            binding.dark = position % 2 == 0
            binding.technical = technical
            binding.executePendingBindings()
        }
    }
}
