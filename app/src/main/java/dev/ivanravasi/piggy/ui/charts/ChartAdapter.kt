package dev.ivanravasi.piggy.ui.charts

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Chart
import dev.ivanravasi.piggy.api.piggy.bodies.entities.ChartKind

class ChartAdapter: ListAdapter<Chart, ChartAdapter.ChartViewHolder>(ChartDiffCallback()) {
    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        val chart = getItem(position)
        holder.hydrate(chart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return if (viewType == ChartKind.LIST.ordinal)
            TopListViewHolder.from(parent)
        else
            LineBarViewHolder.from(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getChartKind().ordinal
    }

    abstract class ChartViewHolder(
        private val viewChartLoader: ChartLoader
    ) : RecyclerView.ViewHolder(viewChartLoader) {
        fun hydrate(chart: Chart) {
            viewChartLoader.hydrateChart(chart)
        }
    }

    class TopListViewHolder private constructor(
        viewChartLoader: ChartLoader
    ): ChartViewHolder(viewChartLoader) {
        companion object {
            fun from(parent: ViewGroup): TopListViewHolder {
                val view = TopListChartLoaderView(parent.context, null)
                view.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                return TopListViewHolder(view)
            }
        }
    }

    class LineBarViewHolder private constructor(
        viewChartLoader: ChartLoader
    ): ChartViewHolder(viewChartLoader) {
        companion object {
            fun from(parent: ViewGroup): LineBarViewHolder {
                val view = VicoChartLoaderView(parent.context, null)
                view.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
                return LineBarViewHolder(view)
            }
        }
    }
}

class ChartDiffCallback : DiffUtil.ItemCallback<Chart>() {
    override fun areItemsTheSame(oldItem: Chart, newItem: Chart): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Chart, newItem: Chart): Boolean {
        return oldItem == newItem
    }
}