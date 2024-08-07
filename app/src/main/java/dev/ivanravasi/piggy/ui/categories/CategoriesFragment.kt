package dev.ivanravasi.piggy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButtonToggleGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentCategoriesBinding
import dev.ivanravasi.piggy.ui.categories.dialogs.ShowCategoryBottomSheet
import dev.ivanravasi.piggy.ui.common.fragments.CRUDFragment
import dev.ivanravasi.piggy.ui.confirmDeleteWithDialog
import dev.ivanravasi.piggy.ui.makeSnackbar

class CategoriesFragment : CRUDFragment<Category, CategoryAdapter.CategoryViewHolder>() {
    private lateinit var viewModel: CategoriesViewModel
    private val adapter = CategoryAdapter { category ->
        ShowCategoryBottomSheet(category, parentFragmentManager, {
            val bundle = Bundle()
            bundle.putString("category", GsonProvider.getSerializer(true).toJson(category))
            findNavController().navigate(R.id.navigation_add_category, bundle)
        }, {
            confirmDeleteWithDialog(it.name) {
                viewModel.delete(category.id, "categories")
                makeSnackbar(binding.root, "Category \"${category.name}\" deleted successfully")
            }
        }).show()
    }
    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        viewModel = CategoriesViewModel(DataStoreRepository(requireContext()), findNavController())

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
            R.id.button_income -> categories.filter { category -> category.type() == CategoryType.IN }
            R.id.button_outcome -> categories.filter { category -> category.type() == CategoryType.OUT }
            else -> categories
        }
        if (list.isEmpty()) {
            adapter.submitList(list)
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.nodata.visibility = View.GONE
            adapter.submitList(list)
        }
    }
}