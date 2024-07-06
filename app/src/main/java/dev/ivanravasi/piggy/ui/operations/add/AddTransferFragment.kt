package dev.ivanravasi.piggy.ui.operations.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransferRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransferBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.operations.index.dialogs.AccountBottomSheet
import dev.ivanravasi.piggy.ui.setAccount


class AddTransferFragment : Fragment() {
    private var transferToUpdate: Transfer? = null
    private lateinit var binding: FragmentAddTransferBinding
    private lateinit var viewModel: AddTransferViewModel
    private lateinit var account: Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransferBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, AddTransferViewModel.Factory(
            DataStoreRepository(requireContext()),
            requireArguments().getLong("id"),
            findNavController()
        )
        )[AddTransferViewModel::class.java]
        account = GsonProvider.getDeserializer().fromJson(requireArguments().getString("account"), Account::class.java)

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputDate.error = it.date.first()
            binding.inputAmount.error = it.amount.first()
            binding.inputNotes.error = it.notes.first()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.buttonAdd.isEnabled = !it

            if (it) binding.loadingProgress.show()
            else {
                binding.loadingProgress.hide()
                if (viewModel.otherAccount.value == null) {
                    backWithSnackbar(binding.root, "You do not have any other accounts to make a transfer with!")
                }
            }
        }

        binding.toggleTransferType.check(R.id.button_outcome)

        viewModel.otherAccount.observe(viewLifecycleOwner) { to ->
            if (to != null)
                setAccount(to)

            updateCheckedSwitchVisibility()
        }

        binding.cardAccount.cardAccount.setOnClickListener {
            AccountBottomSheet(viewModel.accounts.value!!) {
                viewModel.otherAccount.value = it
            }.show(parentFragmentManager, "AccountBottomSheet")
        }

        binding.editDate.setToday()

        val transferStr = arguments?.getString("transfer")
        transferStr?.let {
            transferToUpdate = GsonProvider.getDeserializer().fromJson(it, Transfer::class.java)
            val from = transferToUpdate!!.from
            val to = transferToUpdate!!.to
            if (from == null) {
                viewModel.otherAccount.value = to!!
                binding.toggleTransferType.check(R.id.button_outcome)
            } else {
                viewModel.otherAccount.value = from
                binding.toggleTransferType.check(R.id.button_income)
            }

            binding.editDate.setText(transferToUpdate!!.date)
            binding.editAmount.setText(transferToUpdate!!.amount)
            binding.editNotes.setText(transferToUpdate!!.notes)
            binding.switchChecked.isChecked = transferToUpdate!!.checked

            binding.buttonAdd.text = requireContext().getString(R.string.update_transfer)
            binding.addTitle.text = requireContext().getString(R.string.update_transfer)
        }

        binding.buttonAdd.setOnClickListener {
            val request = TransferRequest(
                if (binding.toggleTransferType.checkedButtonId == R.id.button_income) viewModel.otherAccount.value!!.id else account.id,
                if (binding.toggleTransferType.checkedButtonId == R.id.button_income) account.id else viewModel.otherAccount.value!!.id,
                binding.editDate.date(),
                binding.editAmount.text.toString(),
                binding.editNotes.text.toString(),
                binding.switchChecked.isChecked,
            )
            if (transferToUpdate == null) {
                viewModel.submit(request) {
                    backWithSnackbar(binding.buttonAdd, "Transfer added successfully")
                }
            } else {
                viewModel.update(request, transferToUpdate!!.id) {
                    backWithSnackbar(binding.buttonAdd, "Transfer updated successfully")
                }
            }
        }

        return binding.root
    }

    private fun setAccount(account: Account) {
        binding.cardAccount.setAccount(account)
    }

    private fun updateCheckedSwitchVisibility() {
        if (
            (viewModel.otherAccount.value != null && viewModel.otherAccount.value!!.type == "Bank account")
            || (account.type == "Bank account")
        ) {
            binding.switchChecked.visibility = View.VISIBLE
        } else {
            binding.switchChecked.isChecked = true
            binding.switchChecked.visibility = View.GONE
        }
    }
}