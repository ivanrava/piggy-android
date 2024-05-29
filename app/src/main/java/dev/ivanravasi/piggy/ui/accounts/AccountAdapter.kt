package dev.ivanravasi.piggy.ui.accounts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.ListItemAccountBinding

class AccountAdapter: ListAdapter<Account, AccountAdapter.AccountViewHolder>(AccountDiffCallback()) {
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = getItem(position)
        holder.bind(account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.from(parent)
    }

    class AccountViewHolder private constructor(
        private val binding: ListItemAccountBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            binding.cardAccount.setCardBackgroundColor(Color.parseColor(account.color))

            val color = ContextCompat.getColor(binding.root.context, if (isColorDark(account.color.substring(1))) {
                R.color.account_text_dark
            } else {
                R.color.account_text_light
            })
            binding.accountIcon.loadIconify(account.icon, color)
            binding.accountName.text = account.name
            binding.accountType.text = account.type

            binding.accountName.setTextColor(color)
            binding.accountType.setTextColor(color)
        }

        fun isColorDark(hex: String): Boolean {
            val brightness = Math.round(
                ((Integer.parseInt(hex.substring(0,2), 16) * 299) +
                        (Integer.parseInt(hex.substring(2,4), 16) * 587) +
                        (Integer.parseInt(hex.substring(4,6), 16) * 114)) / 1000.0)

            return brightness > 148
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