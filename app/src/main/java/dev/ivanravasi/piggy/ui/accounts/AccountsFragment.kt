package dev.ivanravasi.piggy.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAccountsBinding
import dev.ivanravasi.piggy.ui.common.fragments.CRUDFragment

class AccountsFragment : CRUDFragment<Account, AccountAdapter.AccountViewHolder>() {
    private lateinit var navController: NavController
    private val adapter = AccountAdapter {
        val bundle = Bundle()
        bundle.putLong("id", it.id)
        navController.navigate(R.id.navigation_operations, bundle)
    }
    private lateinit var binding: FragmentAccountsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        navController = findNavController()
        val viewModel = AccountsViewModel(DataStoreRepository(requireContext()), navController)

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
        val accountsToShow = accounts.filter { account -> account.type in types }

        if (accountsToShow.isEmpty()) {
            adapter.submitList(accountsToShow)
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.nodata.visibility = View.GONE
            adapter.submitList(accountsToShow)
        }
    }
}