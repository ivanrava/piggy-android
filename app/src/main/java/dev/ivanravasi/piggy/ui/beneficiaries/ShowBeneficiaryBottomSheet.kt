package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.databinding.BottomSheetBeneficiaryBinding

class ShowBeneficiaryBottomSheet(
    private val beneficiary: Beneficiary,
    private val fragmentManager: FragmentManager
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetBeneficiaryBinding.inflate(inflater, container, false)

        binding.beneficiaryName.text = beneficiary.name
        binding.beneficiaryType.text = beneficiary.type().uppercase()
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(beneficiary.img, beneficiary.name)

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowBeneficiaryBottomSheet")
    }
}
