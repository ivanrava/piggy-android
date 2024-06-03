package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.BottomSheetPropertyVariationBinding

class VariationBottomSheet(
    private val titleResource: Int,
    private val fragmentManager: FragmentManager
) : BottomSheetDialogFragment() {
    private lateinit var viewModel: VariationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetPropertyVariationBinding.inflate(inflater, container, false)

        viewModel = VariationViewModel(TokenRepository(requireContext()))

        binding.variationTitle.text = getString(titleResource)
        viewModel.date.observe(viewLifecycleOwner) {
            binding.editDate.setText(it)
        }
        binding.editDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select variation date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
            datePicker.show(fragmentManager, "VariationDatePicker")
            datePicker.addOnPositiveButtonClickListener { dateLong ->
                viewModel.updateDate(dateLong)
            }
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "VariationBottomSheet")
    }
}
