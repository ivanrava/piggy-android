package dev.ivanravasi.piggy.ui.properties

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.databinding.ListItemVariationBinding
import dev.ivanravasi.piggy.ui.setCurrency
import dev.ivanravasi.piggy.ui.setDate

class VariationAdapter: ListAdapter<Property.PropertyVariation, VariationAdapter.VariationViewHolder>(PropertyVariationDiffCallback()) {
    override fun onBindViewHolder(holder: VariationViewHolder, position: Int) {
        val variation = getItem(position)
        holder.bind(variation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariationViewHolder {
        return VariationViewHolder.from(parent)
    }

    class VariationViewHolder private constructor(
        private val binding: ListItemVariationBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(variation: Property.PropertyVariation) {
            binding.amount.setCurrency(variation.amount.toDouble() * if (variation.type() == CategoryType.IN) 1 else -1, true)
            binding.value.setCurrency(variation.value.toDouble())
            binding.notes.text = variation.notes
            binding.date.setDate(variation.date)
        }

        companion object {
            fun from(parent: ViewGroup): VariationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemVariationBinding.inflate(layoutInflater, parent, false)
                return VariationViewHolder(binding)
            }
        }
    }
}

class PropertyVariationDiffCallback : DiffUtil.ItemCallback<Property.PropertyVariation>() {
    override fun areItemsTheSame(oldItem: Property.PropertyVariation, newItem: Property.PropertyVariation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Property.PropertyVariation, newItem: Property.PropertyVariation): Boolean {
        return oldItem == newItem
    }
}