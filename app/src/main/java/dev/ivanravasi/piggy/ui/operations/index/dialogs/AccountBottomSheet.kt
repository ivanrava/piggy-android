package dev.ivanravasi.piggy.ui.operations.index.dialogs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.ui.accounts.AccountAdapter
import dev.ivanravasi.piggy.ui.common.dialogs.SearchPickerBottomSheet

class AccountBottomSheet(
    val accounts: List<Account>,
    val onAccountClickListener: (account: Account) -> Unit
) : SearchPickerBottomSheet<Account, AccountAdapter.AccountViewHolder>() {
    override fun createHook() {
        submitListOrNoData(accounts)
    }

    override fun buildAdapter(): ListAdapter<Account, AccountAdapter.AccountViewHolder> {
        return AccountAdapter {
            onAccountClickListener(it)
            dismiss()
        }
    }

    override fun buildLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun performFiltering(term: String): List<Account> {
        return accounts.filter { account -> account.name.lowercase().contains(term.lowercase()) }
    }
}