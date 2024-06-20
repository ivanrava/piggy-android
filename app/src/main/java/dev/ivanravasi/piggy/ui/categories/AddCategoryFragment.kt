package dev.ivanravasi.piggy.ui.categories

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget
import dev.ivanravasi.piggy.api.piggy.bodies.requests.CategoryRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddCategoryBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar


class AddCategoryFragment : Fragment() {
    private lateinit var binding: FragmentAddCategoryBinding
    private lateinit var viewModel: AddCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, AddCategoryViewModel.Factory(
            TokenRepository(requireContext())
        ))[AddCategoryViewModel::class.java]

        binding.toggleCategoryType.check(R.id.button_income)

        binding.pickerIcon.setOnSelectedIconListener {
            viewModel.icon.value = it
        }
        viewModel.icon.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pickerIcon.loadIconify(it)
            }
        }

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputName.error = it.name.first()
            binding.switchVirtual.error = it.virtual.first()
            if (it.icon.first() != null) {
                binding.pickerIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.md_theme_error))
            }
            binding.inputBudgetOverall.error = it.budgetOverall.first()
            binding.inputJan.error = it.budget.jan.first()
            binding.inputFeb.error = it.budget.feb.first()
            binding.inputMar.error = it.budget.mar.first()
            binding.inputApr.error = it.budget.apr.first()
            binding.inputMay.error = it.budget.may.first()
            binding.inputJun.error = it.budget.jun.first()
            binding.inputJul.error = it.budget.jul.first()
            binding.inputAug.error = it.budget.aug.first()
            binding.inputSep.error = it.budget.sep.first()
            binding.inputOct.error = it.budget.oct.first()
            binding.inputNov.error = it.budget.nov.first()
            binding.inputDec.error = it.budget.dec.first()
        }

        viewModel.rootCategories.observe(viewLifecycleOwner) {
            binding.pickerCategory.updateCategories(it)
        }
        binding.pickerCategory.setOnSelectedCategoryListener {
            viewModel.parent.value = it

            if (it != null) {
                if (it.type == "in") {
                    binding.buttonIncome.visibility = View.VISIBLE
                    binding.buttonOutcome.visibility = View.GONE
                    binding.toggleCategoryType.check(R.id.button_income)
                } else {
                    binding.buttonIncome.visibility = View.GONE
                    binding.buttonOutcome.visibility = View.VISIBLE
                    binding.toggleCategoryType.check(R.id.button_outcome)
                }
                binding.switchVirtual.visibility = View.VISIBLE

                binding.layoutBudget.visibility = View.VISIBLE
            } else {
                binding.buttonIncome.visibility = View.VISIBLE
                binding.buttonOutcome.visibility = View.VISIBLE
                binding.toggleCategoryType.check(R.id.button_income)
                binding.switchVirtual.visibility = View.GONE

                binding.layoutBudget.visibility = View.GONE
            }
        }

        binding.chipsBudgetType.setOnCheckedStateChangeListener { group, checkedIds ->
            when (group.checkedChipId) {
                R.id.chip_monthly_custom -> {
                    binding.inputBudgetOverall.visibility = View.GONE
                    binding.gridMonths.visibility = View.VISIBLE
                }
                else -> {
                    binding.inputBudgetOverall.visibility = View.VISIBLE
                    binding.gridMonths.visibility = View.GONE
                }
            }
        }

        binding.buttonAdd.setOnClickListener {
            val request = CategoryRequest(
                binding.editName.text.toString(),
                viewModel.icon.value!!,
                selectedType(),
                viewModel.parent.value?.id,
                binding.switchVirtual.isChecked,
                getOverallBudgetForRequest(),
                buildBudgetForRequest()
            )
            viewModel.submit(request) {
                backWithSnackbar(binding.buttonAdd, "Category added successfully")
            }
        }

        return binding.root
    }

    private fun selectedType(): String {
        return when(binding.toggleCategoryType.checkedButtonId) {
            R.id.button_outcome -> "out"
            R.id.button_income -> "in"
            else -> ""
        }
    }

    private fun getOverallBudgetForRequest(): String? {
        if (viewModel.parent.value == null || binding.chipsBudgetType.checkedChipId != R.id.chip_yearly_fixed)
            return null

        return binding.editBudgetOverall.text.toString()
    }

    private fun buildBudgetForRequest(): Budget? {
        if (viewModel.parent.value == null || binding.chipsBudgetType.checkedChipId == R.id.chip_yearly_fixed)
            return null

        if (binding.chipsBudgetType.checkedChipId == R.id.chip_monthly_fixed)
            return Budget(
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
                binding.editBudgetOverall.toString(),
            )

        return Budget(
            binding.editJan.toString(),
            binding.editFeb.toString(),
            binding.editMar.toString(),
            binding.editApr.toString(),
            binding.editMay.toString(),
            binding.editJun.toString(),
            binding.editJul.toString(),
            binding.editAug.toString(),
            binding.editSep.toString(),
            binding.editOct.toString(),
            binding.editNov.toString(),
            binding.editDec.toString()
        )
    }
}