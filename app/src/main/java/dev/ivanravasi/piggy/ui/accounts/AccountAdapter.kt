package dev.ivanravasi.piggy.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.ListItemAccountBinding
import dev.ivanravasi.piggy.ui.setAccount


class AccountAdapter : ListAdapter<Account, AccountAdapter.AccountViewHolder>(AccountDiffCallback()) {
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = getItem(position)
        holder.bind(account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.from(parent)
    }

    class AccountViewHolder private constructor(
        private val binding: ListItemAccountBinding,
        private val navController: NavController
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            binding.cardAccount.setAccount(account, navController)
        }


        companion object {
            fun from(parent: ViewGroup): AccountViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAccountBinding.inflate(layoutInflater, parent, false)
                val navController = parent.findFragment<AccountsFragment>().findNavController()
                return AccountViewHolder(binding, navController)
            }
        }
    }
}

class AccountDiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
        return oldItem == newItem
    }
}