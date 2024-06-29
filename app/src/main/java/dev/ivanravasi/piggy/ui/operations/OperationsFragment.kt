package dev.ivanravasi.piggy.ui.operations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BudgetSerializer
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentOperationsBinding
import dev.ivanravasi.piggy.ui.accountTextColor
import dev.ivanravasi.piggy.ui.accounts.ShowAccountBottomSheet
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.common.CRUDFragment
import dev.ivanravasi.piggy.ui.makeSnackbar
import dev.ivanravasi.piggy.ui.setCurrency

class OperationsFragment : CRUDFragment<Operation, OperationAdapter.OperationViewHolder>() {
    private val adapter = OperationAdapter({
        val bundle = requireArguments()
        bundle.putString("transaction", GsonBuilder()
            .registerTypeAdapter(CategoryBudget::class.java, BudgetSerializer())
            .create().toJson(it))
        findNavController().navigate(R.id.navigation_add_transaction, bundle)
    }, {
        val bundle = requireArguments()
        bundle.putString("transfer", GsonBuilder().create().toJson(it))
        findNavController().navigate(R.id.navigation_add_transfer, bundle)
    })

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
            binding.total.setCurrency(it.balance.toDouble())
            binding.titleCard.setCardBackgroundColor(Color.parseColor(it.color))
            val textColor = accountTextColor(requireContext(), it.color.substring(1))
            binding.accountName.setTextColor(textColor)
            binding.accountIcon.loadIconify(it.icon, textColor)
            binding.accountType.setTextColor(textColor)
            binding.total.setTextColor(textColor)
            binding.titleCard.visibility = View.VISIBLE

            binding.titleCard.setOnClickListener { _ ->
                ShowAccountBottomSheet(it, parentFragmentManager, {
                    val bundle = Bundle()
                    bundle.putString("account", GsonBuilder()
                        .registerTypeAdapter(CategoryBudget::class.java, BudgetSerializer())
                        .create()
                        .toJson(it)
                    )
                    findNavController().navigate(R.id.navigation_add_account, bundle)
                }, {
                    viewModel.delete(it.id, "accounts")
                    backWithSnackbar(binding.root, "Account \"${it.name}\" deleted successfully")
                }).show()
            }
        }

        return binding.root
    }
}