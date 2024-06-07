package dev.ivanravasi.piggy.ui.properties

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.databinding.ListItemPropertyBinding
import dev.ivanravasi.piggy.ui.setCurrency

class PropertyAdapter: ListAdapter<Property, PropertyAdapter.PropertyViewHolder>(PropertyDiffCallback()) {
    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = getItem(position)
        holder.bind(property)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder.from(parent)
    }

    class PropertyViewHolder private constructor(
        private val binding: ListItemPropertyBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.propertyIcon.loadIconify(property.icon, binding.propertyName.currentTextColor)
            binding.propertyName.text = property.name
            binding.propertyDescription.text = property.description
            if (property.description == null) {
                binding.propertyDescription.visibility = View.GONE
            }
            binding.propertyValue.setCurrency(property.value)

            binding.btnPlus.setOnClickListener {
                val variationBottomSheet = VariationBottomSheet(R.string.title_increment, binding.root.findFragment<PropertiesFragment>().parentFragmentManager)
                variationBottomSheet.show()
            }
            binding.btnMinus.setOnClickListener {
                val variationBottomSheet = VariationBottomSheet(R.string.title_decrement, binding.root.findFragment<PropertiesFragment>().parentFragmentManager)
                variationBottomSheet.show()
            }
        }

        companion object {
            fun from(parent: ViewGroup): PropertyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPropertyBinding.inflate(layoutInflater, parent, false)
                return PropertyViewHolder(binding)
            }
        }
    }
}

class PropertyDiffCallback : DiffUtil.ItemCallback<Property>() {
    override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
        return oldItem == newItem
    }
}