package dev.ivanravasi.piggy.ui.operations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentOperationsBinding
import dev.ivanravasi.piggy.ui.accountTextColor
import dev.ivanravasi.piggy.ui.accounts.ShowAccountBottomSheet
import dev.ivanravasi.piggy.ui.common.CRUDFragment
import dev.ivanravasi.piggy.ui.setCurrency

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

        viewModel.account.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.title.text = "Loading operations..."
                binding.titleCard.visibility = View.GONE
                return@observe
            }

            binding.title.text = "Operations"
            binding.accountName.text = it.name
            binding.accountType.text = it.type
            binding.total.setCurrency(it.total())
            binding.titleCard.setCardBackgroundColor(Color.parseColor(it.color))
            val textColor = accountTextColor(requireContext(), it.color.substring(1))
            binding.accountName.setTextColor(textColor)
            binding.accountIcon.loadIconify(it.icon, textColor)
            binding.accountType.setTextColor(textColor)
            binding.total.setTextColor(textColor)
            binding.titleCard.visibility = View.VISIBLE

            binding.titleCard.setOnClickListener { _ ->
                ShowAccountBottomSheet(it, parentFragmentManager, {

                }, {

                }).show()
            }
        }

        return binding.root
    }
}