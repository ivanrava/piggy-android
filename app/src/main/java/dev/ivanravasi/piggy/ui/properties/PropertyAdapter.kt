package dev.ivanravasi.piggy.ui.properties

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load
import dev.ivanravasi.piggy.R
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
            val iconFields = property.icon.split(":")
            val prefix = iconFields[0]
            val name = iconFields[1]
            val color = String.format("%06X", (0xFFFFFF and binding.propertyName.currentTextColor))
            binding.propertyIcon.load("https://api.iconify.design/$prefix/$name.svg?color=%23$color") {
                size(64)
                decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
                placeholder(R.drawable.ic_properties_24dp)
                crossfade(true)
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