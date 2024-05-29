package dev.ivanravasi.piggy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButtonToggleGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var binding: FragmentCategoriesBinding
    private val adapter = CategoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        viewModel = CategoriesViewModel(TokenRepository(requireContext()))

        binding.listCategories.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            updateList(it, binding.toggleCategoryType)
        }
        binding.toggleCategoryType.addOnButtonCheckedListener { toggleButton, _, _ ->
            viewModel.categories.value?.let { updateList(it, toggleButton) }
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