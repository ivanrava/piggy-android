package dev.ivanravasi.piggy.ui.charts.vico

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.Zoom
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.views.cartesian.CartesianChartView
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.ChartBinding
import dev.ivanravasi.piggy.ui.charts.ChartLoader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val LINE_MAX_HISTORY = 12

class VicoChartLoaderView(
    context: Context,
    attrs: AttributeSet?
): ChartLoader(context, attrs) {
    private var chartView: CartesianChartView
    private val binding: ChartBinding =
        ChartBinding.inflate(LayoutInflater.from(context), this, true)
    private var viewModel: ChartLoaderViewModel = ChartLoaderViewModel(
        DataStoreRepository(context),
        getFragmentManager()!!.findFragmentById(R.id.nav_host_fragment_activity_main)!!.findNavController()
    )
    private lateinit var chartData: Chart

    init {
        chartView = binding.chartHost
        chartView.modelProducer = CartesianChartModelProducer.build()

        viewModel.chartName.observe(context as LifecycleOwner) {
            binding.chartTitle.text = it
        }

        viewModel.stats.observe(context as LifecycleOwner) {
            if (it.isEmpty())
                return@observe

            val statsIn = it.filter { it.type == "in" }.takeLast(LINE_MAX_HISTORY)
            val statsOut = it.filter { it.type == "out" }.takeLast(LINE_MAX_HISTORY)
            val outTimestamps = statsOut.map { it.time }
            val inTimestamps = statsIn.map { it.time }
            val timestamps = (outTimestamps + inTimestamps).distinct().sorted()
            val toSubtract: Float = if (chartData.interval == "year") timestamps.min().toFloat() else 0f
            chartView.modelProducer!!.tryRunTransaction {
                if (chartData.kind == "line") {
                    lineSeries {
                        if (statsOut.isNotEmpty()) {
                            series(
                                y = statsOut.map { it.getStat(chartData.stat).toDouble() },
                                x = outTimestamps.map { if (chartData.interval == "month") timestamps.indexOf(it) else it.toFloat() - toSubtract }
                            )
                        }
                        if (statsIn.isNotEmpty()) {
                            series(
                                y = statsIn.map { it.getStat(chartData.stat).toDouble() },
                                x = inTimestamps.map { if (chartData.interval == "month") timestamps.indexOf(it) else it.toFloat() - toSubtract }
                            )
                        }
                    }
                } else {
                    columnSeries {
                        // TODO: factor duplicate
                        if (statsOut.isNotEmpty()) {
                            series(
                                y = statsOut.map { it.getStat(chartData.stat).toDouble() },
                                x = outTimestamps.map { if (chartData.interval == "month") timestamps.indexOf(it) else it.toFloat() - toSubtract }
                            )
                        }
                        if (statsIn.isNotEmpty()) {
                            series(
                                y = statsIn.map { it.getStat(chartData.stat).toDouble() },
                                x = inTimestamps.map { if (chartData.interval == "month") timestamps.indexOf(it) else it.toFloat() - toSubtract }
                            )
                        }
                    }
                }
            }
            (chartView.chart?.bottomAxis as HorizontalAxis<AxisPosition.Horizontal.Bottom>).apply {
                valueFormatter = CartesianValueFormatter { x, chartValues, _ ->
                    if (chartData.interval == "year")
                        (x + toSubtract).toInt().toString()
                    else
                        LocalDate.parse(timestamps[x.toInt()].replace(" ", "T").substring(0, 10)).format(DateTimeFormatter.ofPattern("MMM y"))
                }
            }
            chartView.scrollHandler = ScrollHandler(initialScroll = Scroll.Absolute.End)
            chartView.zoomHandler = ZoomHandler(initialZoom = Zoom.x(5f))
        }
    }

    override fun hydrateChart(chartData: Chart) {
        this.chartData = chartData
        viewModel.requestChart(chartData)
    }

    private fun getFragmentManager(): FragmentManager? {
        return if (context is FragmentActivity)
            (context as FragmentActivity).supportFragmentManager
        else
            null
    }
}