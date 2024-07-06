package dev.ivanravasi.piggy.ui.operations.add

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.requests.TransactionRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddTransactionBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar
import dev.ivanravasi.piggy.ui.operations.add.dialogs.SearchBeneficiaryBottomSheet


class AddTransactionFragment : Fragment() {
    private var transactionToUpdate: Transaction? = null
    private lateinit var binding: FragmentAddTransactionBinding
    private lateinit var defaultCardStrokeColor: ColorStateList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        val account = GsonProvider.getDeserializer().fromJson(requireArguments().getString("account"), Account::class.java)
        val viewModel = ViewModelProvider(this, AddTransactionViewModel.Factory(
            DataStoreRepository(requireContext()),
            account,
            findNavController()
        )
        )[AddTransactionViewModel::class.java]

        defaultCardStrokeColor = binding.cardBeneficiary.beneficiaryCard.strokeColorStateList!!

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
                binding.cardBeneficiary.beneficiaryCard.setStrokeColor(defaultCardStrokeColor)
            } else {
                binding.beneficiaryName.text = getString(R.string.no_beneficiary_selected)
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
            SearchBeneficiaryBottomSheet(viewModel.beneficiaries.value!!) {
                viewModel.beneficiary.value = it
            }.show(parentFragmentManager, "BeneficiaryBottomSheet")
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            binding.pickCategory.updateCategories(it)
        }
        binding.pickCategory.setOnSelectedCategoryListener {
            viewModel.category.value = it
        }

        binding.editDate.setToday()

        val transactionStr = arguments?.getString("transaction")
        transactionStr?.let {
            transactionToUpdate = GsonProvider.getDeserializer().fromJson(it, Transaction::class.java)
            viewModel.beneficiary.value = transactionToUpdate!!.beneficiary
            viewModel.category.value = transactionToUpdate!!.category

            binding.editDate.setText(transactionToUpdate!!.date)
            binding.editAmount.setText(transactionToUpdate!!.amount)
            binding.editNotes.setText(transactionToUpdate!!.notes)
            binding.switchChecked.isChecked = transactionToUpdate!!.checked

            binding.buttonAdd.text = requireContext().getString(R.string.update_transaction)
            binding.addTitle.text = requireContext().getString(R.string.update_transaction)
        }

        binding.buttonAdd.setOnClickListener {
            binding.cardBeneficiary.beneficiaryCard.setStrokeColor(defaultCardStrokeColor)
            binding.pickCategory.setError(null)
            if (viewModel.beneficiary.value == null) {
                binding.cardBeneficiary.beneficiaryCard.setStrokeColor(ColorStateList.valueOf(requireContext().getColor(R.color.md_theme_error)))
                return@setOnClickListener
            }
            if (viewModel.category.value == null) {
                binding.pickCategory.setError("Category could not be empty")
                return@setOnClickListener
            }
            val request = TransactionRequest(
                account.id,
                viewModel.beneficiary.value!!,
                viewModel.category.value!!,
                binding.editDate.date(),
                binding.switchChecked.isChecked,
                binding.editAmount.text.toString(),
                binding.editNotes.text.toString(),
            )
            if (transactionToUpdate == null) {
                viewModel.submit(request) {
                    backWithSnackbar(binding.buttonAdd, "Transaction added successfully")
                }
            } else {
                viewModel.update(request, transactionToUpdate!!.id) {
                    backWithSnackbar(binding.buttonAdd, "Transaction updated successfully")
                }
            }
        }

        return binding.root
    }

    private fun setBeneficiary(beneficiary: Beneficiary) {
        binding.beneficiaryName.text = beneficiary.name
        binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(beneficiary.img, beneficiary.name)
    }
}