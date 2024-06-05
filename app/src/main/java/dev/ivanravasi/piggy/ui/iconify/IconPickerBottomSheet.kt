package dev.ivanravasi.piggy.ui.iconify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.databinding.BottomSheetIconPickerBinding

class IconPickerBottomSheet(val color: Int, val onIconClickListener: OnIconClickListener) : BottomSheetDialogFragment() {
    private val SPAN_COUNT = 6

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetIconPickerBinding.inflate(inflater, container, false)
        val viewModel = IconPickerViewModel()

        binding.loadingProgress.hide()

        val manager = GridLayoutManager(activity, SPAN_COUNT)
        binding.gridIcons.layoutManager = manager
        val adapter = IconAdapter(color, object : OnIconClickListener {
            override fun onIconClick(icon: String) {
                onIconClickListener.onIconClick(icon)
                dismiss()
            }
        })
        binding.gridIcons.adapter = adapter

        binding.editSearch.doAfterTextChanged {
            viewModel.queryIcons(it.toString())
        }
        viewModel.icons.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }

        return binding.root
    }
}