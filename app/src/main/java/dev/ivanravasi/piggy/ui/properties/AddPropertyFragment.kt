package dev.ivanravasi.piggy.ui.properties

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddPropertyBinding
import dev.ivanravasi.piggy.ui.iconify.IconPickerBottomSheet
import dev.ivanravasi.piggy.ui.iconify.OnIconClickListener

class AddPropertyFragment : Fragment() {
    private lateinit var viewModel: AddPropertyViewModel
    private lateinit var binding: FragmentAddPropertyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        viewModel = AddPropertyViewModel(TokenRepository(requireContext()))

        DrawableCompat.setTint(binding.pickerIcon.drawable, binding.addPropertyTitle.currentTextColor)
        binding.pickerIcon.setOnClickListener {
            binding.pickerIcon.imageTintList = null
            IconPickerBottomSheet(binding.addPropertyTitle.currentTextColor, object : OnIconClickListener {
                override fun onIconClick(icon: String) {
                    viewModel.icon.value = icon
                }
            }).show(parentFragmentManager, "PropertyIconPicker")
        }

        viewModel.icon.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pickerIcon.loadIconify(it, binding.addPropertyTitle.currentTextColor)
            }
        }

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputName.error = it.name.first()
            binding.inputInitialValue.error = it.initialValue.first()
            binding.inputDescription.error = it.description.first()
            if (it.icon.first() != null) {
                binding.pickerIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.md_theme_error))
            }
        }

        binding.buttonAddProperty.setOnClickListener {
            viewModel.submit(
                binding.editName.text.toString(),
                binding.editInitialValue.text.toString(),
                binding.editDescription.text.toString()
            ) {
                Snackbar.make(
                    binding.buttonAddProperty,
                    "Property added successfully",
                    Snackbar.LENGTH_SHORT
                ).setAnchorView(R.id.bottom_bar)
                    .show()
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}