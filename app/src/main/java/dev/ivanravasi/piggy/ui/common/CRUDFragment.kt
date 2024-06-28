package dev.ivanravasi.piggy.ui.common

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textview.MaterialTextView

open class CRUDFragment<T, Q : ViewHolder?>: Fragment() {
    fun setup(
        list: RecyclerView,
        adapter: ListAdapter<T, Q>,
        viewModel: IndexApiViewModel<T>,
        noDataView: MaterialTextView,
        loadingProgressIndicator: LinearProgressIndicator,
        onUpdateList: (List<T>) -> Unit
    ) {
        list.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                noDataView.visibility = View.GONE
                loadingProgressIndicator.show()
            } else loadingProgressIndicator.hide()
        }
        viewModel.objList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                noDataView.visibility = View.VISIBLE
            }
            onUpdateList(it)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}