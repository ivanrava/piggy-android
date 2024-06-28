package dev.ivanravasi.piggy.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.databinding.BottomSheetCategoryBinding

val MONTHS = listOf("January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December")

class ShowCategoryBottomSheet(
    private val category: Category,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (category: Category) -> Unit,
    private val onDelete: (category: Category) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetCategoryBinding.inflate(inflater, container, false)

        binding.categoryName.text = category.name
        binding.categoryType.text = when (category.type()) {
            CategoryType.OUT -> requireContext().getString(R.string.type_out)
            CategoryType.IN -> requireContext().getString(R.string.type_in)
        }.uppercase()
        binding.categoryIcon.loadIconify(category.icon, binding.categoryName.currentTextColor)

        val budgetFillPairs = category.fills()
        category.fills().forEachIndexed { index, pair ->
            val budgetBar = BudgetBarView(binding.layoutBudget.context, null)
            budgetBar.setTypeColor(category.type())
            budgetBar.setPercentage(pair.first, pair.second, height = 8f, paddingVertical = 2, showLabels = true)
            if (category.fills().count() > 1)
                budgetBar.setMonth(MONTHS[index])
            binding.layoutBudget.addView(budgetBar)
        }
        if (budgetFillPairs.isEmpty())
            binding.layoutBudget.visibility = View.GONE

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(category)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(category)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowCategoryBottomSheet")
    }
}
