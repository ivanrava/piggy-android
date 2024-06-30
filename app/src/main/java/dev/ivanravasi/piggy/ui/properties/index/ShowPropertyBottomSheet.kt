package dev.ivanravasi.piggy.ui.properties.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.databinding.BottomSheetPropertyBinding
import dev.ivanravasi.piggy.ui.properties.index.variations.VariationAdapter

class ShowPropertyBottomSheet(
    private val property: Property,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (property: Property) -> Unit,
    private val onDelete: (property: Property) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetPropertyBinding.inflate(inflater, container, false)

        binding.propertyName.text = property.name
        binding.propertyDescription.text = property.description
        binding.propertyIcon.loadIconify(property.icon, binding.propertyName.currentTextColor)

        val adapter = VariationAdapter()
        binding.listVariations.adapter = adapter
        adapter.submitList(property.variations)

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(property)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(property)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowPropertyBottomSheet")
    }
}
