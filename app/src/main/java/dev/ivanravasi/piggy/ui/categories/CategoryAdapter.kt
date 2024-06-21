package dev.ivanravasi.piggy.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryBudget
import dev.ivanravasi.piggy.databinding.ListItemCategoryBinding

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
        holder.bind(category, categoryClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder.from(parent)
    }

    class CategoryViewHolder private constructor(
        private val binding: ListItemCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        private val adapterChildren = CategoryAdapter()
        private var isOpened: Boolean = false

        fun bind(category: Category, listener: OnCategoryClickListener) {
            binding.categoryIcon.loadIconify(category.icon, binding.categoryName.currentTextColor)
            binding.categoryName.text = category.name
            if (category.parent == null && category.parentCategoryId == null) {
                binding.categoryDescription.text =
                    binding.root.context.getString(R.string.children_categories, category.children.count())
                binding.children.adapter = adapterChildren
                adapterChildren.submitList(null)
                binding.cardCategory.setOnClickListener {
                    adapterChildren.submitList(if (isOpened) null else category.children.map {
                        it.parent = category
                        return@map it
                    })
                    isOpened = !isOpened
                }
            } else {
                binding.cardCategory.setCardBackgroundColor(binding.root.context.getColor(R.color.md_theme_surfaceContainer))
                binding.categoryDescription.text = budgetText(category)
                binding.children.adapter = null
            }
        }

        private fun budgetText(category: Category): String {
            return when (category.budget) {
                is CategoryBudget.Monthly -> (category.budget as CategoryBudget.Monthly).value.toString()
                is CategoryBudget.Yearly -> "Yearly"
            }
        }

        companion object {
            fun from(parent: ViewGroup): CategoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(layoutInflater, parent, false)
                return CategoryViewHolder(binding)
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