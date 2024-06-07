package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentOperationsBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment

class OperationsFragment : CRUDFragment<Operation, OperationAdapter.OperationViewHolder>() {
    private val adapter = OperationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOperationsBinding.inflate(inflater, container, false)
        val viewModel = OperationsViewModel(TokenRepository(requireContext()), requireArguments().getLong("id"))

        binding.listOperations.adapter = adapter
        setup(
            list = binding.listOperations,
            adapter = adapter,
            viewModel = viewModel,
            noDataView = binding.nodata,
            loadingProgressIndicator = binding.loadingProgress
        ) {
            adapter.submitList(it)
        }

        viewModel.accountName.observe(viewLifecycleOwner) {
            binding.title.text = it.ifEmpty { "Loading operations..." }
        }

        return binding.root
    }
}