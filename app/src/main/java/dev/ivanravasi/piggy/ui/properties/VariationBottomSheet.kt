package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
        binding.editDate.setToday()

        return binding.root
    }

    fun show() {
        show(fragmentManager, "VariationBottomSheet")
    }
}
