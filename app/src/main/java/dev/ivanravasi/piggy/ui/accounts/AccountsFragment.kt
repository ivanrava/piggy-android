package dev.ivanravasi.piggy.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.ChipGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAccountsBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment

class AccountsFragment : CRUDFragment<Account, AccountAdapter.AccountViewHolder>() {
    private val adapter = AccountAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAccountsBinding.inflate(inflater, container, false)
        val viewModel = AccountsViewModel(TokenRepository(requireContext()))

        setup(
            list = binding.listAccounts,
            adapter = adapter,
            viewModel = viewModel,
            noDataView = binding.nodata,
            loadingProgressIndicator = binding.loadingProgress
        ) {
            updateList(it, binding.chipAccountType)
        }

        binding.chipAccountType.setOnCheckedStateChangeListener { group, checkedIds ->
            viewModel.objList.value?.let { updateList(it, group) }
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