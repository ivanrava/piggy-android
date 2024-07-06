package dev.ivanravasi.piggy.ui.operations.add.dialogs

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.ui.beneficiaries.BeneficiaryAdapter
import dev.ivanravasi.piggy.ui.common.dialogs.SearchPickerBottomSheet

class SearchBeneficiaryBottomSheet(
    val beneficiaries: List<Beneficiary>,
    val onBeneficiaryClickListener: (beneficiary: Beneficiary) -> Unit
) : SearchPickerBottomSheet<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>() {
    private val SPAN_COUNT = 6

    override fun createHook() {
        submitListOrNoData(beneficiaries)
    }

    override fun buildAdapter(): ListAdapter<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder> {
        return BeneficiaryAdapter {
            onBeneficiaryClickListener(it)
            dismiss()
        }
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