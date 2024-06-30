package dev.ivanravasi.piggy.ui.charts.toplist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.ChartTopListBinding
import dev.ivanravasi.piggy.ui.charts.ChartLoader
import dev.ivanravasi.piggy.ui.charts.vico.ChartLoaderViewModel

class TopListChartLoaderView(
    context: Context,
    attrs: AttributeSet?
): ChartLoader(context, attrs) {
    private var chartData: Chart? = null
    private val viewModel: ChartLoaderViewModel = ChartLoaderViewModel(
        TokenRepository(context),
        getFragmentManager()!!.findFragmentById(R.id.nav_host_fragment_activity_main)!!.findNavController()
    )
    val binding = ChartTopListBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        viewModel.chartName.observe(context as LifecycleOwner) {
            binding.chartTitle.text = it
        }

        viewModel.stats.observe(context as LifecycleOwner) {
            if (chartData == null)
                return@observe

            val adapter = when (chartData!!.filter) {
                "accounts" -> TopListAccountAdapter(chartData!!.stat)
                "categories" -> TopListCategoryAdapter(chartData!!.stat)
                else -> TopListBeneficiaryAdapter(chartData!!.stat)
            }
            binding.chartList.adapter = adapter
            adapter.submitList(it)
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