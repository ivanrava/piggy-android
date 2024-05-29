package dev.ivanravasi.piggy.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButtonToggleGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAccountsBinding

class AccountsFragment : Fragment() {
    private lateinit var viewModel: AccountsViewModel
    private lateinit var binding: FragmentAccountsBinding
    private val adapter = AccountAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        viewModel = AccountsViewModel(TokenRepository(requireContext()))

        binding.listAccounts.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
        viewModel.accounts.observe(viewLifecycleOwner) {
            updateList(it, binding.toggleAccountType)
        }
        binding.toggleAccountType.addOnButtonCheckedListener { toggleButton, _, _ ->
            viewModel.accounts.value?.let { updateList(it, toggleButton) }
        }

        return binding.root
    }

    private fun updateList(accounts: List<Account>, toggleButton: MaterialButtonToggleGroup) {
        val selection = toggleButton.checkedButtonId
        val list = when (selection) {
            R.id.button_cash -> accounts.filter { account -> account.type == "Cash" }
            R.id.button_voucher -> accounts.filter { account -> account.type == "Voucher" }
            R.id.button_bank_account -> accounts.filter { account -> account.type == "Bank account" }
            R.id.button_investments -> accounts.filter { account -> account.type == "Investments" }
            else -> accounts
        }
        adapter.submitList(list)
    }
}