package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.databinding.BottomSheetPropertyVariationBinding
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
            binding.propertyValue.setCurrency(property.value)

            binding.btnPlus.setOnClickListener {
                val variationBottomSheet = VariationBottomSheet(R.string.title_increment)
                variationBottomSheet.show(binding.root.findFragment<PropertiesFragment>().parentFragmentManager, "IncrementBottomSheet")
            }
            binding.btnMinus.setOnClickListener {
                val variationBottomSheet = VariationBottomSheet(R.string.title_decrement)
                variationBottomSheet.show(binding.root.findFragment<PropertiesFragment>().parentFragmentManager, "DecrementBottomSheet")
            }
        }

        companion object {
            fun from(parent: ViewGroup): PropertyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPropertyBinding.inflate(layoutInflater, parent, false)
                return PropertyViewHolder(binding)
            }
        }

        class VariationBottomSheet(private val titleResource: Int) : BottomSheetDialogFragment() {
            override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View {
                val binding = BottomSheetPropertyVariationBinding.inflate(inflater, container, false)
                binding.variationTitle.text = getString(titleResource)
                return binding.root
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