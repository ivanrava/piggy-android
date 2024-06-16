package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransactionBinding
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener


class AddTransactionFragment : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddTransactionViewModel.Factory(
            TokenRepository(requireContext())
        ))[AddTransactionViewModel::class.java]

        viewModel.errors.observe(viewLifecycleOwner) {
            // TODO: handle validation errors
        }

        viewModel.beneficiaries.observe(viewLifecycleOwner) {
            val suggestion = it.firstOrNull()
            suggestion?.let {
                setBeneficiary(it)
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            val suggestion = it.firstOrNull()
            suggestion?.let {
                setCategory(it)
            }
        }

        binding.cardBeneficiary.beneficiaryImg.setOnClickListener {
            BeneficiaryBottomSheet(viewModel.beneficiaries.value!!, object :
                OnBeneficiaryClickListener {
                override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                    setBeneficiary(beneficiary)
                }
            }).show(parentFragmentManager, "BeneficiaryBottomSheet")
        }

        binding.editDate.setToday()

        binding.buttonAdd.setOnClickListener {
//            val request = TransactionRequest(
//                binding.editName.text.toString(),
//                viewModel.icon.value,
//                "#${viewModel.color.value?.hexCode}",
//                binding.editOpening.date(),
//                binding.editClosing.date(),
//                binding.editInitialBalance.text.toString(),
//                binding.editDescription.text.toString(),
//                viewModel.beneficiaries.value?.find { it.type == binding.editAccountType.text.toString() }?.id!!
//            )
//            viewModel.submit(request) {
//                backWithSnackbar(binding.buttonAdd, "Transaction added successfully")
//            }
        }

        return binding.root
    }

    private fun setBeneficiary(beneficiary: Beneficiary) {
        binding.beneficiaryName.text = beneficiary.name
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(beneficiary.img, beneficiary.name)
    }

    private fun setCategory(category: Category) {
        binding.categoryName.text = category.name
        binding.categoryIcon.loadIconify(category.icon, binding.categoryName.currentTextColor)
    }
}