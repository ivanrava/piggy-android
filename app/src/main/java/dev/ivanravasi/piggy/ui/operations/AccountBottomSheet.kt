package dev.ivanravasi.piggy.ui.operations

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.ui.accounts.AccountAdapter
import dev.ivanravasi.piggy.ui.accounts.OnAccountClickListener
import dev.ivanravasi.piggy.ui.common.SearchPickerBottomSheet

class AccountBottomSheet(
    val accounts: List<Account>,
    val onAccountClickListener: OnAccountClickListener
) : SearchPickerBottomSheet<Account, AccountAdapter.AccountViewHolder>() {
    override fun createHook() {
        submitListOrNoData(accounts)
    }

    override fun buildAdapter(): ListAdapter<Account, AccountAdapter.AccountViewHolder> {
        return AccountAdapter(object : OnAccountClickListener {
            override fun onAccountClick(account: Account) {
                onAccountClickListener.onAccountClick(account)
                dismiss()
            }
        })
    }

    override fun buildLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun performFiltering(term: String): List<Account> {
        return accounts.filter { account -> account.name.lowercase().contains(term.lowercase()) }
    }
}