package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransferBinding
import dev.ivanravasi.piggy.ui.accounts.OnAccountClickListener
import dev.ivanravasi.piggy.ui.setAccount


class AddTransferFragment : Fragment() {
    private lateinit var binding: FragmentAddTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransferBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddTransferViewModel.Factory(
            TokenRepository(requireContext())
        ))[AddTransferViewModel::class.java]

        viewModel.errors.observe(viewLifecycleOwner) {
            // TODO: handle validation errors
        }

        viewModel.accounts.observe(viewLifecycleOwner) {
            val suggestion = it.firstOrNull()
            suggestion?.let {
                setAccount(it)
            }
        }

        binding.cardAccount.cardAccount.setOnClickListener {
            AccountBottomSheet(viewModel.accounts.value!!, object : OnAccountClickListener {
                override fun onAccountClick(account: Account) {
                    setAccount(account)
                }
            }).show(parentFragmentManager, "AccountBottomSheet")
        }

        binding.editDate.setToday()

        binding.buttonAdd.setOnClickListener {
//            val request = TransactionRequest(
//                binding.editName.text.toString(),
//                viewModel.icon.value,
//                "#${viewModel.color.value?.hexCode}",
//                binding.editOpening.date(),
//                binding.editClosing.date(),
//                binding.editInitialBalance.text.toString(),
//                binding.editDescription.text.toString(),
//                viewModel.beneficiaries.value?.find { it.type == binding.editAccountType.text.toString() }?.id!!
//            )
//            viewModel.submit(request) {
//                backWithSnackbar(binding.buttonAdd, "Transaction added successfully")
//            }
        }

        return binding.root
    }

    private fun setAccount(account: Account) {
        binding.cardAccount.setAccount(account)
    }
}