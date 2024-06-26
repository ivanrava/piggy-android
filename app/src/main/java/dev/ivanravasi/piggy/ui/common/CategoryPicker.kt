package dev.ivanravasi.piggy.ui.common

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Category
import dev.ivanravasi.piggy.ui.categories.OnCategoryClickListener
import dev.ivanravasi.piggy.ui.operations.CategoryBottomSheet

class CategoryPicker(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {
    private val categoryName: MaterialTextView
    private val categoryIcon: ShapeableImageView
    private val categoryHint: MaterialTextView
    private val card: MaterialCardView

    private val listeners: MutableList<(category: Category?) -> Unit> = mutableListOf()
    private var categories: List<Category> = emptyList()

    init {
        inflate(context, R.layout.pick_category, this)
        categoryName = findViewById(R.id.category_name)
        categoryIcon = findViewById(R.id.category_icon)
        categoryHint = findViewById(R.id.category_edit_hint)
        card = findViewById(R.id.card_category)

        categoryName.text = context.getString(R.string.category_picker_no_selection)

        card.setOnClickListener {
            getFragmentManager()?.let { fragmentManager ->
                CategoryBottomSheet(categories, object : OnCategoryClickListener {
                    override fun onCategoryClick(category: Category) {
                        setCategory(category)
                        if (card.isLongClickable) {
                            categoryHint.text = context.getString(
                                R.string.hint_long_press_to_clear,
                                categoryHint.text
                            )
                        }
                        listeners.forEach { it(category) }
                    }
                }).show(fragmentManager, "CategoryBottomSheet")
            }
        }
        card.setOnLongClickListener {
            categoryName.text = context.getString(R.string.category_picker_no_selection)
            categoryHint.text = context.getString(R.string.card_edit_hint)
            categoryIcon.setImageResource(android.R.color.transparent)
            listeners.forEach { it(null) }
            return@setOnLongClickListener true
        }
    }

    private fun getFragmentManager(): FragmentManager? {
        return if (context is FragmentActivity)
            (context as FragmentActivity).supportFragmentManager
        else
            null
    }

    fun setCategory(category: Category) {
        categoryName.text = category.name
        categoryIcon.loadIconify(category.icon, categoryName.currentTextColor)
        listeners.forEach { it(category) }
    }

    fun setOnSelectedCategoryListener(listener: (category: Category?) -> Unit) {
        listeners.add(listener)
    }

    fun updateCategories(categories: List<Category>) {
        this.categories = categories
    }

    fun disableDeselection() {
        card.isLongClickable = false
    }
}