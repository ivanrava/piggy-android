package dev.ivanravasi.piggy.ui.beneficiaries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.ivanravasi.piggy.api.dicebear.loadAvatar
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.databinding.ListItemBeneficiaryBinding

class BeneficiaryAdapter: ListAdapter<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>(BeneficiaryDiffCallback()) {
    override fun onBindViewHolder(holder: BeneficiaryViewHolder, position: Int) {
        val beneficiary = getItem(position)
        holder.bind(beneficiary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryViewHolder {
        return BeneficiaryViewHolder.from(parent)
    }

    class BeneficiaryViewHolder private constructor(
        private val binding: ListItemBeneficiaryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(beneficiary: Beneficiary) {
            if (beneficiary.img.contains("://")) {
                binding.beneficiaryImg.load(beneficiary.img) {
                    size(128)
                    crossfade(true)
                    listener(onError = {_, _ ->
                        binding.beneficiaryImg.loadAvatar(style = "initials", seed = beneficiary.name)
                    })
                }
            }
            else
                binding.beneficiaryImg.loadAvatar(style = beneficiary.img, seed = beneficiary.name)
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
