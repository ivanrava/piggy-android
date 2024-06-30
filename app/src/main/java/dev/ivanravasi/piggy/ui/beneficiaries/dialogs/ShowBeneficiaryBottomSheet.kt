package dev.ivanravasi.piggy.ui.beneficiaries.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BeneficiaryType
import dev.ivanravasi.piggy.databinding.BottomSheetBeneficiaryBinding

class ShowBeneficiaryBottomSheet(
    private val beneficiary: Beneficiary,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (beneficiary: Beneficiary) -> Unit,
    private val onDelete: (beneficiary: Beneficiary) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetBeneficiaryBinding.inflate(inflater, container, false)

        binding.beneficiaryName.text = beneficiary.name
        binding.beneficiaryType.text = when (beneficiary.type()) {
            BeneficiaryType.PEOPLE -> requireContext().getString(R.string.type_people)
            BeneficiaryType.COMPANY -> requireContext().getString(R.string.type_companies)
            BeneficiaryType.GENERIC -> requireContext().getString(R.string.type_generic)
        }.uppercase()
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(beneficiary.img, beneficiary.name)

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(beneficiary)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(beneficiary)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowBeneficiaryBottomSheet")
    }
}
