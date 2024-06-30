package dev.ivanravasi.piggy.ui.categories.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setPadding
import com.google.android.material.textview.MaterialTextView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.ui.setCurrency

class BudgetBarView(
    context: Context,
    attrs: AttributeSet?,
): LinearLayout(context, attrs) {
    private val fg: View
    private val bg: View
    private val currentFill: MaterialTextView
    private val maxFill: MaterialTextView
    private val budgetBarMask: LinearLayout
    private val monthText: MaterialTextView

    init {
        inflate(context, R.layout.budget_bar, this)
        fg = findViewById(R.id.budget_bar_fg)
        bg = findViewById(R.id.budget_bar_bg)
        currentFill = findViewById(R.id.current_fill)
        maxFill = findViewById(R.id.max_fill)
        monthText = findViewById(R.id.month)

        budgetBarMask = findViewById(R.id.budget_bar_mask)
        budgetBarMask.clipToOutline = true
    }

    private fun setBudgetBarPercentage(percentage: Float, height: Float = 4f) {
        fg.layoutParams = LayoutParams(
            0,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.resources.displayMetrics).toInt(),
            percentage
        )
        bg.layoutParams = LayoutParams(
            0,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, context.resources.displayMetrics).toInt(),
            1-percentage
        )
    }

    fun setPercentage(
        currentValue: Double,
        maxValue: Double,
        height: Float = 4f,
        paddingVertical: Int = 0,
        showLabels: Boolean = false
    ) {
        setBudgetBarPercentage(safeDivision(currentValue, maxValue), height)
        setPadding(0, paddingVertical, 0, paddingVertical)
        if (showLabels) {
            currentFill.setCurrency(currentValue)
            maxFill.setCurrency(maxValue)
        } else {
            currentFill.visibility = View.GONE
            maxFill.visibility = View.GONE
            budgetBarMask.setPadding(0)
        }
    }

    private fun safeDivision(over: Double, under: Double): Float {
        alpha = 1f
        return if (under.toInt() == 0 && over > 0) {
            1f
        } else if (under.toInt() == 0 && over.toInt() == 0) {
            alpha = 0.2f
            0f
        } else {
            (over / under).toFloat()
        }
    }

    fun setTypeColor(type: CategoryType) {
        fg.background = AppCompatResources.getDrawable(
            context,
            if (type == CategoryType.IN) R.drawable.budget_bar_income else R.drawable.budget_bar_outcome
        )
        bg.background = fg.background
    }

    fun hide() {
        visibility = View.INVISIBLE
        currentFill.visibility = View.GONE
        maxFill.visibility = View.GONE
        budgetBarMask.setPadding(0)
    }

    fun setMonth(month: String) {
        monthText.text = month
        monthText.visibility = View.VISIBLE
    }
}