package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransactionBinding


class AddTransactionFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddTransactionViewModel.Factory(
            TokenRepository(requireContext())
        ))[AddTransactionViewModel::class.java]

        viewModel.errors.observe(viewLifecycleOwner) {
            // TODO: handle validation errors
        }

        viewModel.beneficiaries.observe(viewLifecycleOwner) {
            val suggestion = it.firstOrNull()
            suggestion?.let {
                binding.beneficiaryName.text = it.name
                binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(it.img, it.name)
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            val suggestion = it.firstOrNull()
            suggestion?.let {
                binding.categoryName.text = it.name
                binding.categoryIcon.loadIconify(it.icon, binding.categoryName.currentTextColor)
            }
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
}