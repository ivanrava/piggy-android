package dev.ivanravasi.piggy.ui.properties.index.variations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyVariationRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.BottomSheetPropertyVariationBinding

class VariationBottomSheet(
    private val propertyId: Long,
    private val type: CategoryType,
    private val fragmentManager: FragmentManager,
    private val onSuccess: () -> Unit = {}
) : BottomSheetDialogFragment() {
    private lateinit var viewModel: VariationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetPropertyVariationBinding.inflate(inflater, container, false)

        viewModel = VariationViewModel(TokenRepository(requireContext()), findNavController())

        binding.variationTitle.text = getString(when (type) {
            CategoryType.IN -> R.string.title_increment
            CategoryType.OUT -> R.string.title_decrement
        })
        binding.editDate.setToday()

        binding.buttonAdd.setOnClickListener {
            val request = PropertyVariationRequest(
                propertyId,
                binding.editDate.date(),
                type.textual,
                binding.editAmount.text.toString(),
                binding.editNotes.text.toString()
            )
            viewModel.submit(request) {
                dismiss()
                onSuccess()
            }
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "VariationBottomSheet")
    }
}
