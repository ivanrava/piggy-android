package dev.ivanravasi.piggy.ui.common.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.databinding.BottomSheetSearchPickerBinding
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced

abstract class SearchPickerBottomSheet<EntityType, Q: RecyclerView.ViewHolder?>(

): BottomSheetDialogFragment() {
    protected lateinit var adapter: ListAdapter<EntityType, Q>
    protected lateinit var binding: BottomSheetSearchPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = buildAdapter()
        binding = BottomSheetSearchPickerBinding.inflate(inflater, container, false)
        binding.gridIcons.adapter = adapter
        binding.gridIcons.layoutManager = buildLayoutManager()

        binding.loadingProgress.hide()

        createHook()

        binding.editSearch.afterTextChangedDebounced {
            submitListOrNoData(performFiltering(it))
        }

        return binding.root
    }

    abstract fun createHook()

    abstract fun buildAdapter(): ListAdapter<EntityType, Q>

    abstract fun buildLayoutManager(): LayoutManager

    abstract fun performFiltering(term: String): List<EntityType>

    fun submitListOrNoData(list: List<EntityType>) {
        if (list.isEmpty()) {
            adapter.submitList(list)
            binding.nodata.visibility = View.VISIBLE
        } else {
            binding.nodata.visibility = View.GONE
            adapter.submitList(list)
        }
    }
}