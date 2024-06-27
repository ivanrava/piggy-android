package dev.ivanravasi.piggy.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BudgetDeserializer
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransactionBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.beneficiaries.OnBeneficiaryClickListener


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
            binding.inputDate.error = it.date.first()
            binding.inputAmount.error = it.amount.first()
            binding.inputNotes.error = it.notes.first()
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
                binding.pickCategory.setCategory(it)
            }
        }

        binding.pickCategory.disableDeselection()

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
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.pickCategory.updateCategories(it)
        }
        binding.pickCategory.setOnSelectedCategoryListener {
            viewModel.category.value = it
        }

        binding.editDate.setToday()

        val transactionStr = arguments?.getString("transaction")
        transactionStr.let {
            val transaction = GsonBuilder()
                .registerTypeAdapter(CategoryBudget::class.java, BudgetDeserializer())
                .create()
                .fromJson(it, Transaction::class.java)
            viewModel.beneficiary.value = transaction.beneficiary
            viewModel.category.value = transaction.category

            binding.editDate.setText(transaction.date)
            binding.editAmount.setText(transaction.amount)
            binding.editNotes.setText(transaction.notes)

            binding.buttonAdd.text = requireContext().getString(R.string.update_transaction)
            binding.addTitle.text = requireContext().getString(R.string.update_transaction)
        }

        binding.buttonAdd.setOnClickListener {
            val request = TransactionRequest(
                requireArguments().getLong("id"),
                viewModel.beneficiary.value!!,
                viewModel.category.value!!,
                binding.editDate.date(),
                binding.switchChecked.isChecked,
                binding.editAmount.text.toString(),
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
}