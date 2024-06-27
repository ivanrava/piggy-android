package dev.ivanravasi.piggy.ui.categories

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.databinding.ListItemCategoryBinding
import dev.ivanravasi.piggy.ui.toCurrency

interface OnCategoryClickListener {
    fun onCategoryClick(category: Category)
}

class CategoryAdapter(
    private val categoryClickListener: OnCategoryClickListener = object : OnCategoryClickListener {
        override fun onCategoryClick(category: Category) {
            TODO("Not yet implemented")
        }
    }
): ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent, categoryClickListener)
    }

    class CategoryViewHolder private constructor(
        private val binding: ListItemCategoryBinding,
        private val listener: OnCategoryClickListener
    ): RecyclerView.ViewHolder(binding.root) {
        private val adapterChildren = CategoryAdapter(listener)
        private var isOpened: Boolean = false

        fun bind(category: Category) {
            binding.categoryIcon.loadIconify(category.icon, binding.categoryName.currentTextColor)
            binding.categoryName.text = category.name
            if (category.parent == null && category.parentCategoryId == null) {
                binding.categoryDescription.text =
                    binding.root.context.getString(R.string.children_categories, category.children.count())
                binding.children.adapter = adapterChildren
                adapterChildren.submitList(null)
                binding.btnShowChildren.setOnClickListener {
                    adapterChildren.submitList(if (isOpened) null else category.children.map {
                        it.parent = category
                        return@map it
                    })
                    binding.btnShowChildren.icon = AppCompatResources.getDrawable(
                        binding.root.context,
                        if (isOpened) R.drawable.ic_collapse_open_24 else R.drawable.ic_collapse_close_24
                    )
                    isOpened = !isOpened
                }
                binding.budgetBar.visibility = View.INVISIBLE
            } else {
                binding.btnShowChildren.visibility = View.GONE
                binding.cardCategory.setCardBackgroundColor(binding.root.context.getColor(R.color.md_theme_surfaceContainer))
                binding.children.adapter = null

                if (category.budget != null && category.expenditures != null) {
                    setBudgetText(binding.categoryDescription, category)
                    binding.budgetBar.visibility = View.VISIBLE
                    binding.budgetBar.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        if (category.type() == CategoryType.IN) R.drawable.budget_bar_income else R.drawable.budget_bar_outcome
                    )
                    binding.budgetBarBack.background = binding.budgetBar.background
                    setBudgetBarPercentage(calculateBudgetBarPercentage(category))
                } else {
                    binding.categoryDescription.text = category.parent!!.name
                }
            }
            binding.cardCategory.setOnClickListener {
                listener.onCategoryClick(category)
            }
        }

        private fun calculateBudgetBarPercentage(category: Category): Float {
            fun safeDivision(over: Double, under: Double): Float {
                return if (under.toInt() == 0 && over > 0) {
                    1f
                } else {
                    (over / under).toFloat()
                }
            }

            return when (category.budget!!) {
                is CategoryBudget.Monthly -> safeDivision(
                    category.expenditures!!.currentMonthValue(),
                    (category.budget as CategoryBudget.Monthly).value.currentMonthValue()
                )
                is CategoryBudget.Yearly -> safeDivision(
                    category.expenditures!!.sum(),
                    (category.budget as CategoryBudget.Yearly).value.toDouble()
                )
            }
        }

        private fun setBudgetBarPercentage(percentage: Float) {
            binding.budgetBar.layoutParams = LinearLayout.LayoutParams(
                0,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, binding.root.context.resources.displayMetrics).toInt(),
                percentage
            )
            binding.budgetBarBack.layoutParams = LinearLayout.LayoutParams(
                0,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, binding.root.context.resources.displayMetrics).toInt(),
                1-percentage
            )
        }

        private fun setBudgetText(categoryDescription: MaterialTextView, category: Category) {
            categoryDescription.text = when (category.budget!!) {
                is CategoryBudget.Monthly -> "Monthly. This month: ${category.expenditures!!.currentMonthValue().toCurrency()} / ${(category.budget as CategoryBudget.Monthly).value.currentMonthValue().toCurrency()}"
                is CategoryBudget.Yearly -> "Yearly: ${category.expenditures!!.sum().toCurrency()} / ${(category.budget as CategoryBudget.Yearly).value.toDouble().toCurrency()}"
            }
            if (categoryDescription.text.endsWith(" ${0.0.toCurrency()} / ${0.0.toCurrency()}")) {
                categoryDescription.setTextColor(ColorUtils.setAlphaComponent(categoryDescription.currentTextColor, 128))
            }
        }

        companion object {
            fun from(parent: ViewGroup, listener: OnCategoryClickListener): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding, listener)
            }
        }
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}