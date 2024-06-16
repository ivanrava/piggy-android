package dev.ivanravasi.piggy.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
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
        fun bind(category: Category, listener: OnCategoryClickListener) {
            binding.categoryIcon.loadIconify(category.icon, binding.categoryName.currentTextColor)
            binding.categoryName.text = category.name
            binding.categoryDescription.text = "${category.children.count()} children categories"
            binding.cardCategory.setOnClickListener {
                listener.onCategoryClick(category)
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