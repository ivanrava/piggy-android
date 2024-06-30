package dev.ivanravasi.piggy.ui.charts.toplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.databinding.TopListItemCategoryBinding
import dev.ivanravasi.piggy.ui.setCurrency

class TopListCategoryAdapter(
    private val requestedStat: String,
) : ListAdapter<Stat, TopListCategoryAdapter.CategoryViewHolder>(StatDiffCallback()) {
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryStat = getItem(position)
        holder.bind(categoryStat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent, requestedStat)
    }

    class CategoryViewHolder private constructor(
        private val binding: TopListItemCategoryBinding,
        private val requestedStat: String
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: Stat) {
            val color = binding.root.context.getColor(if (stat.type == "in") R.color.in_value else R.color.out_value)
            binding.categoryIcon.loadIconify(stat.icon!!, color)
            binding.categoryName.text = stat.name
            binding.categoryName.setTextColor(color)
            if (requestedStat != "count") {
                binding.statNumber.setCurrency(stat.getStat(requestedStat).toDouble())
            } else {
                binding.statNumber.text = stat.getStat(requestedStat).toInt().toString()
            }
            binding.statNumber.setTextColor(color)
        }

        companion object {
            fun from(parent: ViewGroup, requestedStat: String): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopListItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding, requestedStat)
            }
        }
    }
}