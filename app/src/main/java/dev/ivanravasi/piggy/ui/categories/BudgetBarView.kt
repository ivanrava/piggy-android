package dev.ivanravasi.piggy.ui.categories

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType

class BudgetBarView(
    context: Context,
    attrs: AttributeSet?,
): LinearLayout(context, attrs) {
    private val fg: View
    private val bg: View

    init {
        inflate(context, R.layout.budget_bar, this)
        fg = findViewById(R.id.budget_bar_fg)
        bg = findViewById(R.id.budget_bar_bg)
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
        showIfEmpty: Boolean = false
    ) {
        setBudgetBarPercentage(safeDivision(currentValue, maxValue), height)
        setPadding(0, paddingVertical, 0, paddingVertical)
    }

    private fun safeDivision(over: Double, under: Double): Float {
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
}