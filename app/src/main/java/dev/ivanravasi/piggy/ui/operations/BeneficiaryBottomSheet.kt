package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.databinding.BottomSheetIconPickerBinding
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced
import dev.ivanravasi.piggy.ui.beneficiaries.BeneficiaryAdapter
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener

class BeneficiaryBottomSheet(
    val beneficiaries: List<Beneficiary>,
    val onBeneficiaryClickListener: OnBeneficiaryClickListener
) : BottomSheetDialogFragment() {
    private val SPAN_COUNT = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetIconPickerBinding.inflate(inflater, container, false)

        binding.loadingProgress.hide()

        val manager = GridLayoutManager(activity, SPAN_COUNT)
        binding.gridIcons.layoutManager = manager
        val adapter = BeneficiaryAdapter(object : OnBeneficiaryClickListener {
            override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                onBeneficiaryClickListener.onBeneficiaryClick(beneficiary)
                dismiss()
            }
        })
        binding.gridIcons.adapter = adapter

        adapter.submitList(beneficiaries)

        binding.editSearch.afterTextChangedDebounced {
            adapter.submitList(beneficiaries.filter { beneficiary -> beneficiary.name.lowercase().contains(it.lowercase()) })
        }

        return binding.root
    }
}