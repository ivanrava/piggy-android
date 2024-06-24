package dev.ivanravasi.piggy.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.ListItemAccountBinding
import dev.ivanravasi.piggy.ui.setAccount

interface OnAccountClickListener {
    fun onAccountClick(account: Account)
}

class AccountAdapter(
    private val accountClickListener: OnAccountClickListener = object : OnAccountClickListener {
        override fun onAccountClick(account: Account) {
            TODO("Not yet implemented")
        }
    }
) : ListAdapter<Account, AccountAdapter.AccountViewHolder>(AccountDiffCallback()) {
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = getItem(position)
        holder.bind(account, accountClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.from(parent)
    }

    class AccountViewHolder private constructor(
        private val binding: ListItemAccountBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account, listener: OnAccountClickListener) {
            binding.cardAccount.setAccount(account)
            binding.cardAccount.cardAccount.setOnClickListener {
                listener.onAccountClick(account)
            }
        }


        companion object {
            fun from(parent: ViewGroup): AccountViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAccountBinding.inflate(layoutInflater, parent, false)
                return AccountViewHolder(binding)
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