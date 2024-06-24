package dev.ivanravasi.piggy.ui.charts

import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart

interface ChartLoadable {
    fun hydrateChart(chartData: Chart)
}