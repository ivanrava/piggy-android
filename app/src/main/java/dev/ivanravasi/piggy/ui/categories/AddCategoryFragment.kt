package dev.ivanravasi.piggy.ui.categories

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Budget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BudgetDeserializer
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
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
        // Initialize binding and view model
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, AddCategoryViewModel.Factory(
            TokenRepository(requireContext())
        ))[AddCategoryViewModel::class.java]

        // Set default initial values
        binding.toggleCategoryType.check(R.id.button_income)

        // Set listeners / observers
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
                if (it.type() == CategoryType.IN) {
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

        // Set initial values from editable (if present)
        val categoryStr = arguments?.getString("category")
        categoryStr.let {
            val category = GsonBuilder()
                .registerTypeAdapter(CategoryBudget::class.java, BudgetDeserializer())
                .create().fromJson(it, Category::class.java)

            binding.addTitle.text = requireContext().getString(R.string.button_update_category)
            binding.buttonAdd.text = requireContext().getString(R.string.button_update_category)
            binding.editName.setText(category.name)
            binding.pickerIcon.loadIconify(category.icon)
            if (category.parent != null) {
                binding.pickerCategory.setCategory(category.parent!!)
                binding.switchVirtual.isChecked = category.virtual
                binding.chipMonthlyFixed.visibility = View.GONE
                when (category.budget!!) {
                    is CategoryBudget.Monthly -> {
                        binding.chipsBudgetType.check(R.id.chip_monthly_custom)
                        val budget: Budget = (category.budget as CategoryBudget.Monthly).value
                        binding.editJan.setText(budget.jan)
                        binding.editFeb.setText(budget.feb)
                        binding.editMar.setText(budget.mar)
                        binding.editApr.setText(budget.apr)
                        binding.editMay.setText(budget.may)
                        binding.editJun.setText(budget.jun)
                        binding.editJul.setText(budget.jul)
                        binding.editAug.setText(budget.aug)
                        binding.editSep.setText(budget.sep)
                        binding.editOct.setText(budget.oct)
                        binding.editNov.setText(budget.nov)
                        binding.editDec.setText(budget.dec)
                    }
                    is CategoryBudget.Yearly -> {
                        binding.chipsBudgetType.check(R.id.chip_yearly_fixed)
                        binding.editBudgetOverall.setText(category.budget!!.value.toString())
                    }
                }
            } else {
                binding.toggleCategoryType.check(if (category.type() == CategoryType.IN) R.id.button_income else R.id.button_outcome)
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