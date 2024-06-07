package dev.ivanravasi.piggy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButtonToggleGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentCategoriesBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment

class CategoriesFragment : CRUDFragment<Category, CategoryAdapter.CategoryViewHolder>() {
    private val adapter = CategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val viewModel = CategoriesViewModel(TokenRepository(requireContext()))

        setup(
            list = binding.listCategories,
            adapter = adapter,
            viewModel = viewModel,
            noDataView = binding.nodata,
            loadingProgressIndicator = binding.loadingProgress
        ) {
            updateList(it, binding.toggleCategoryType)
        }

        binding.toggleCategoryType.addOnButtonCheckedListener { toggleButton, _, _ ->
            viewModel.objList.value?.let { updateList(it, toggleButton) }
        }

        return binding.root
    }

    private fun updateList(categories: List<Category>, toggleButton: MaterialButtonToggleGroup) {
        val selection = toggleButton.checkedButtonId
        val list = when (selection) {
            R.id.button_income -> categories.filter { category -> category.type == "in" }
            R.id.button_outcome -> categories.filter { category -> category.type == "out" }
            else -> categories
        }
        adapter.submitList(list)
    }
}