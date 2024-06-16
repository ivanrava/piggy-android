package dev.ivanravasi.piggy.ui.beneficiaries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.databinding.ListItemBeneficiaryBinding

interface OnBeneficiaryClickListener {
    fun onBeneficiaryClick(beneficiary: Beneficiary)
}


class BeneficiaryAdapter(
    private val beneficiaryClickListener: OnBeneficiaryClickListener = object :
        OnBeneficiaryClickListener {
        override fun onBeneficiaryClick(beneficiary: Beneficiary) {
            TODO("Not yet implemented")
        }
    }
): ListAdapter<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>(BeneficiaryDiffCallback()) {
    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        val beneficiary = getItem(position)
        holder.bind(beneficiary, beneficiaryClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryViewHolder {
        return BeneficiaryViewHolder.from(parent)
    }

    class BeneficiaryViewHolder private constructor(
        private val binding: ListItemBeneficiaryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(beneficiary: Beneficiary, listener: OnBeneficiaryClickListener) {
            binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(img = beneficiary.img, seed = beneficiary.name)
            binding.cardBeneficiary.beneficiaryImg.setOnClickListener {
                listener.onBeneficiaryClick(beneficiary)
            }
        }

        companion object {
            fun from(parent: ViewGroup): BeneficiaryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBeneficiaryBinding.inflate(layoutInflater, parent, false)
                return BeneficiaryViewHolder(binding)
            }
        }
    }
}

class BeneficiaryDiffCallback : DiffUtil.ItemCallback<Beneficiary>() {
    override fun areItemsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Beneficiary, newItem: Beneficiary): Boolean {
        return oldItem == newItem
    }
}
