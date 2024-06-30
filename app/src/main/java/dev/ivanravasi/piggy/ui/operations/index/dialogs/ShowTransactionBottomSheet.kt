package dev.ivanravasi.piggy.ui.operations.index.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BeneficiaryType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.databinding.BottomSheetTransactionBinding
import dev.ivanravasi.piggy.ui.setCurrency
import dev.ivanravasi.piggy.ui.setDate

class ShowTransactionBottomSheet(
    private val transaction: Transaction,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (transaction: Transaction) -> Unit,
    private val onDelete: (transaction: Transaction) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetTransactionBinding.inflate(inflater, container, false)

        binding.beneficiaryName.text = transaction.beneficiary.name
        binding.beneficiaryType.text = when (transaction.beneficiary.type()) {
            BeneficiaryType.PEOPLE -> requireContext().getString(R.string.type_people)
            BeneficiaryType.COMPANY -> requireContext().getString(R.string.type_companies)
            BeneficiaryType.GENERIC -> requireContext().getString(R.string.type_generic)
        }.uppercase()
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(transaction.beneficiary.img, transaction.beneficiary.name)

        binding.categoryName.text = transaction.category.name
        binding.categoryIcon.loadIconify(transaction.category.icon, binding.categoryName.currentTextColor)

        binding.amount.setCurrency(transaction.amount.toDouble() * if (transaction.category.type() == CategoryType.IN) 1 else -1, true)
        binding.date.setDate(transaction.date)

        binding.notes.text = transaction.notes
        if (transaction.notes == null || transaction.notes.isEmpty()) {
            binding.notes.visibility = View.GONE
        }

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(transaction)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(transaction)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowTransactionBottomSheet")
    }
}
