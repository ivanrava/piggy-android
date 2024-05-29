package dev.ivanravasi.piggy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        viewModel = CategoriesViewModel(TokenRepository(requireContext()))

        val adapter = CategoryAdapter()
        binding.listCategories.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}