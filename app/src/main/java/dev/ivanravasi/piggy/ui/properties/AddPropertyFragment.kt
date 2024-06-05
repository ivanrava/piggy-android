package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
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
            IconPickerBottomSheet(binding.addPropertyTitle.currentTextColor, object : OnIconClickListener {
                override fun onIconClick(icon: String) {
                    binding.pickerIcon.loadIconify(icon, binding.addPropertyTitle.currentTextColor)
                }
            }).show(parentFragmentManager, "PropertyIconPicker")
        }

        return binding.root
    }
}