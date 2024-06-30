package dev.ivanravasi.piggy.ui.common.iconify

import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.ui.common.dialogs.SearchPickerBottomSheet

class IconPickerBottomSheet(
    val color: Int,
    val onIconClickListener: OnIconClickListener
) : SearchPickerBottomSheet<String, IconAdapter.IconifyIconViewHolder>() {
    private val SPAN_COUNT = 6
    private lateinit var viewModel: IconPickerViewModel

    override fun createHook() {
        viewModel = IconPickerViewModel()

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.icons.observe(viewLifecycleOwner) {
            submitListOrNoData(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
    }

    override fun buildAdapter(): ListAdapter<String, IconAdapter.IconifyIconViewHolder> {
        return IconAdapter(color, object : OnIconClickListener {
            override fun onIconClick(icon: String) {
                onIconClickListener.onIconClick(icon)
                dismiss()
            }
        })
    }

    override fun buildLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(activity, SPAN_COUNT)
    }

    override fun performFiltering(term: String): List<String> {
        viewModel.queryIcons(term)
        return viewModel.icons.value!!
    }
}