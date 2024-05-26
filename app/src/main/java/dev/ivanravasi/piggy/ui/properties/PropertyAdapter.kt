package dev.ivanravasi.piggy.ui.properties

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.bodies.entities.Property
import dev.ivanravasi.piggy.databinding.ListItemPropertyBinding

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
            binding.propertyName.text = property.name
            binding.propertyDescription.text = property.description
            binding.propertyValue.text = property.value.toString()
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