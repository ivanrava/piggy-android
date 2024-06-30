package dev.ivanravasi.piggy.ui.charts.toplist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Stat
import dev.ivanravasi.piggy.databinding.TopListItemBeneficiaryBinding
import dev.ivanravasi.piggy.ui.setCurrency

class TopListBeneficiaryAdapter(
    private val requestedStat: String,
) : ListAdapter<Stat, TopListBeneficiaryAdapter.BeneficiaryViewHolder>(StatDiffCallback()) {
    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        val beneficiaryStat = getItem(position)
        holder.bind(beneficiaryStat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryViewHolder {
        return BeneficiaryViewHolder.from(parent, requestedStat)
    }

    class BeneficiaryViewHolder private constructor(
        private val binding: TopListItemBeneficiaryBinding,
        private val requestedStat: String
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(stat: Stat) {
            binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(stat.img!!, stat.name!!)
            binding.beneficiaryName.text = stat.name
            if (requestedStat != "count") {
                binding.statNumber.setCurrency(stat.getStat(requestedStat).toDouble())
            } else {
                binding.statNumber.text = stat.getStat(requestedStat).toInt().toString()
            }
        }

        companion object {
            fun from(parent: ViewGroup, requestedStat: String): BeneficiaryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TopListItemBeneficiaryBinding.inflate(layoutInflater, parent, false)
                return BeneficiaryViewHolder(binding, requestedStat)
            }
        }
    }
}