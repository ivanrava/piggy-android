package dev.ivanravasi.piggy.ui.charts.toplistadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.databinding.TopListItemAccountBinding
import dev.ivanravasi.piggy.ui.setAccount
import dev.ivanravasi.piggy.ui.setCurrency

interface OnAccountClickListener {
    fun onAccountClick(account: Account)
}

class TopListAccountAdapter(
    private val requestedStat: String,
    private val accountClickListener: OnAccountClickListener = object : OnAccountClickListener {
        override fun onAccountClick(account: Account) {
            TODO("Not yet implemented")
        }
    }
) : ListAdapter<Stat, TopListAccountAdapter.AccountViewHolder>(StatDiffCallback()) {
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val accountStat = getItem(position)
        holder.bind(accountStat, accountClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder.from(parent, requestedStat)
    }

    class AccountViewHolder private constructor(
        private val binding: TopListItemAccountBinding,
        private val requestedStat: String
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: Stat, listener: OnAccountClickListener) {
            binding.cardAccount.setAccount(stat)
            if (requestedStat != "count") {
                binding.statNumber.setCurrency(stat.getStat(requestedStat).toDouble())
            } else {
                binding.statNumber.text = stat.getStat(requestedStat).toInt().toString()
            }
//            binding.cardAccount.cardAccount.setOnClickListener {
//                listener.onAccountClick(stat)
//            }
        }

        companion object {
            fun from(parent: ViewGroup, requestedStat: String): AccountViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopListItemAccountBinding.inflate(layoutInflater, parent, false)
                return AccountViewHolder(binding, requestedStat)
            }
        }
    }
}

class StatDiffCallback : DiffUtil.ItemCallback<Stat>() {
    override fun areItemsTheSame(oldItem: Stat, newItem: Stat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stat, newItem: Stat): Boolean {
        return oldItem == newItem
    }
}