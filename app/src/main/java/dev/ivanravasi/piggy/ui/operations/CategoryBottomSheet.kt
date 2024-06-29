package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.databinding.BottomSheetIconPickerBinding
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced
import dev.ivanravasi.piggy.ui.categories.CategoryAdapter
import dev.ivanravasi.piggy.ui.categories.OnCategoryClickListener

class CategoryBottomSheet(
    val categories: List<Category>,
    val onCategoryClickListener: OnCategoryClickListener
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetIconPickerBinding.inflate(inflater, container, false)

        binding.loadingProgress.hide()

        binding.gridIcons.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(object : OnCategoryClickListener {
            override fun onCategoryClick(category: Category) {
                onCategoryClickListener.onCategoryClick(category)
                dismiss()
            }
        }, true)
        binding.gridIcons.adapter = adapter

        adapter.submitList(categories)

        binding.editSearch.afterTextChangedDebounced {
            adapter.submitList(categories.filter { category ->
                if (category.name.lowercase().contains(it.lowercase())) {
                    return@filter true
                }
                category.parent?.let { parent ->
                    if (parent.name.lowercase().contains(it.lowercase()))
                        return@filter true
                }
                return@filter false
            })
        }

        return binding.root
    }
}