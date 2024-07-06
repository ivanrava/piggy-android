package dev.ivanravasi.piggy.ui.operations.add.dialogs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.ui.categories.CategoryAdapter
import dev.ivanravasi.piggy.ui.common.dialogs.SearchPickerBottomSheet

class SearchCategoryBottomSheet(
    val categories: List<Category>,
    val onCategoryClickListener: (category: Category) -> Unit
) : SearchPickerBottomSheet<Category, CategoryAdapter.CategoryViewHolder>() {
    override fun createHook() {
        submitListOrNoData(categories)
    }

    override fun buildAdapter(): ListAdapter<Category, CategoryAdapter.CategoryViewHolder> {
        return CategoryAdapter(true) {
            onCategoryClickListener(it)
            dismiss()
        }
    }

    override fun buildLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun performFiltering(term: String): List<Category> {
        return categories.filter { category ->
            if (category.name.lowercase().contains(term.lowercase())) {
                return@filter true
            }
            category.parent?.let { parent ->
                if (parent.name.lowercase().contains(term.lowercase()))
                    return@filter true
            }
            return@filter false
        }
    }
}