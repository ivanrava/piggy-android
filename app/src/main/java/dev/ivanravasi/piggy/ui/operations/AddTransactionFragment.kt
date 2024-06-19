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
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransactionBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener
import dev.ivanravasi.piggy.ui.categories.OnCategoryClickListener


class AddTransactionFragment : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddTransactionViewModel.Factory(
            TokenRepository(requireContext()),
            // FIXME: rename to account_id
            requireArguments().getLong("id")
        ))[AddTransactionViewModel::class.java]

        viewModel.errors.observe(viewLifecycleOwner) {
            // TODO: handle validation errors
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.buttonAdd.isEnabled = !it

            if (it) binding.loadingProgress.show()
            else binding.loadingProgress.hide()
        }

        viewModel.beneficiary.observe(viewLifecycleOwner) {
            if (it != null) {
                setBeneficiary(it)
            }
        }
        viewModel.category.observe(viewLifecycleOwner) {
            if (it != null) {
                setCategory(it)
            }
        }

        viewModel.account.observe(viewLifecycleOwner) {
            if (it != null && it.type != "Bank account") {
                binding.switchChecked.isChecked = true
                binding.switchChecked.visibility = View.GONE
            }
        }

        binding.cardBeneficiary.beneficiaryImg.setOnClickListener {
            BeneficiaryBottomSheet(viewModel.beneficiaries.value!!, object : OnBeneficiaryClickListener {
                override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                    viewModel.beneficiary.value = beneficiary
                }
            }).show(parentFragmentManager, "BeneficiaryBottomSheet")
        }
        binding.cardCategory.setOnClickListener {
            CategoryBottomSheet(viewModel.categories.value!!, object : OnCategoryClickListener {
                override fun onCategoryClick(category: Category) {
                    viewModel.category.value = category
                }
            }).show(parentFragmentManager, "CategoryBottomSheet")
        }

        binding.editDate.setToday()

        binding.buttonAdd.setOnClickListener {
            val request = TransactionRequest(
                requireArguments().getLong("id"),
                viewModel.beneficiary.value!!,
                viewModel.category.value!!,
                binding.editDate.date(),
                binding.switchChecked.isChecked,
                binding.editValue.text.toString(),
                binding.editNotes.text.toString(),
            )
            viewModel.submit(request) {
                backWithSnackbar(binding.buttonAdd, "Transaction added successfully")
            }
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