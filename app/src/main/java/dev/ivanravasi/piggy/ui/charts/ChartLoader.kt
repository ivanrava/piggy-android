package dev.ivanravasi.piggy.ui.charts

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart

abstract class ChartLoader(
    context: Context,
    attrs: AttributeSet?
): LinearLayout(context, attrs) {
    abstract fun hydrateChart(chartData: Chart)
}