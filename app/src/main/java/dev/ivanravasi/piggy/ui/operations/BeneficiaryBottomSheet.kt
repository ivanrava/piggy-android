package dev.ivanravasi.piggy.ui.operations

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.ui.beneficiaries.BeneficiaryAdapter
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener
import dev.ivanravasi.piggy.ui.common.SearchPickerBottomSheet

class BeneficiaryBottomSheet(
    val beneficiaries: List<Beneficiary>,
    val onBeneficiaryClickListener: OnBeneficiaryClickListener
) : SearchPickerBottomSheet<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>() {
    private val SPAN_COUNT = 6

    override fun createHook() {
        submitListOrNoData(beneficiaries)
    }

    override fun buildAdapter(): ListAdapter<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder> {
        return BeneficiaryAdapter(object : OnBeneficiaryClickListener {
            override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                onBeneficiaryClickListener.onBeneficiaryClick(beneficiary)
                dismiss()
            }
        })
    }

    override fun buildLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(activity, SPAN_COUNT)
    }

    override fun performFiltering(term: String): List<Beneficiary> {
        return beneficiaries.filter { beneficiary ->
            beneficiary.name.lowercase().contains(term.lowercase())
        }
    }
}