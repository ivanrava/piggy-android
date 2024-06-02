package dev.ivanravasi.piggy.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
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
            updateList(it, binding.chipAccountType)
        }
        binding.chipAccountType.setOnCheckedStateChangeListener { group, checkedIds ->
            viewModel.accounts.value?.let { updateList(it, group) }
        }

        return binding.root
    }

    private fun updateList(accounts: List<Account>, chipGroup: ChipGroup) {
        val selections = chipGroup.checkedChipIds
        val idType = mapOf(
            R.id.chip_cash to "Cash",
            R.id.chip_voucher to "Voucher",
            R.id.chip_bank_account to "Bank account",
            R.id.chip_investments to "Investments"
        )
        val types = selections.map { chipId -> idType[chipId] }
        adapter.submitList(accounts.filter { account -> account.type in types })
    }
}