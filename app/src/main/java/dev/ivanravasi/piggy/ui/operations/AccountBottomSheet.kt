package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.BottomSheetIconPickerBinding
import dev.ivanravasi.piggy.ui.accounts.AccountAdapter
import dev.ivanravasi.piggy.ui.accounts.OnAccountClickListener
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced

class AccountBottomSheet(
    val accounts: List<Account>,
    val onAccountClickListener: OnAccountClickListener
) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetIconPickerBinding.inflate(inflater, container, false)

        binding.loadingProgress.hide()

        binding.gridIcons.layoutManager = LinearLayoutManager(context)
        val adapter = AccountAdapter(object : OnAccountClickListener {
            override fun onAccountClick(account: Account) {
                onAccountClickListener.onAccountClick(account)
                dismiss()
            }
        })
        binding.gridIcons.adapter = adapter

        adapter.submitList(accounts)

        binding.editSearch.afterTextChangedDebounced {
            adapter.submitList(accounts.filter { account -> account.name.lowercase().contains(it.lowercase()) })
        }

        return binding.root
    }
}