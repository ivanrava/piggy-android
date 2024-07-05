package dev.ivanravasi.piggy.ui.operations.index

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentOperationsBinding
import dev.ivanravasi.piggy.ui.accountTextColor
import dev.ivanravasi.piggy.ui.accounts.dialogs.ShowAccountBottomSheet
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.common.fragments.CRUDFragment
import dev.ivanravasi.piggy.ui.makeSnackbar
import dev.ivanravasi.piggy.ui.operations.index.dialogs.ShowTransactionBottomSheet
import dev.ivanravasi.piggy.ui.operations.index.dialogs.ShowTransferBottomSheet
import dev.ivanravasi.piggy.ui.setCurrency

class OperationsFragment : CRUDFragment<Operation, OperationAdapter.OperationViewHolder>() {
    private lateinit var viewModel: OperationsViewModel
    private lateinit var binding: FragmentOperationsBinding
    private val adapter = OperationAdapter({
        ShowTransactionBottomSheet(it, parentFragmentManager, {
            val bundle = requireArguments()
            bundle.putString("transaction", GsonProvider.getSerializer().toJson(it))
            bundle.putString("account", GsonProvider.getSerializer().toJson(viewModel.account.value))
            findNavController().navigate(R.id.navigation_add_transaction, bundle)
        }, {
            viewModel.delete(it.id, "transactions")
            makeSnackbar(binding.root, "Transaction deleted successfully")
        }).show()
    }, {
        ShowTransferBottomSheet(viewModel.account.value!!, it, parentFragmentManager, {
            val bundle = requireArguments()
            bundle.putString("transfer", GsonProvider.getSerializer().toJson(it))
            bundle.putString("account", GsonProvider.getSerializer().toJson(viewModel.account.value))
            findNavController().navigate(R.id.navigation_add_transfer, bundle)
        }, {
            viewModel.delete(it.id, "transfers")
            makeSnackbar(binding.root, "Transfer deleted successfully")
        }).show()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOperationsBinding.inflate(inflater, container, false)
        val account = GsonProvider.getDeserializer().fromJson(requireArguments().getString("account"), Account::class.java)
        viewModel = OperationsViewModel(
            DataStoreRepository(requireContext()),
            account.id,
            findNavController()
        )

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
                    bundle.putString("account", GsonProvider.getSerializer().toJson(it))
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