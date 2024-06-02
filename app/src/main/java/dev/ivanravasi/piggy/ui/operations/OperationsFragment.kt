package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentOperationsBinding

class OperationsFragment : Fragment() {
    private lateinit var viewModel: OperationsViewModel
    private lateinit var binding: FragmentOperationsBinding
    private val adapter = OperationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperationsBinding.inflate(inflater, container, false)
        viewModel = OperationsViewModel(TokenRepository(requireContext()), requireArguments().getLong("id"))

        binding.listOperations.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
        viewModel.operations.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.accountName.observe(viewLifecycleOwner) {
            binding.title.text = it.ifEmpty { "Loading operations..." }
        }

        return binding.root
    }
}