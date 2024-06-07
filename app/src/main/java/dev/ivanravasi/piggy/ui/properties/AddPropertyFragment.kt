package dev.ivanravasi.piggy.ui.properties

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddPropertyBinding

class AddPropertyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        val viewModel = AddPropertyViewModel(TokenRepository(requireContext()))

        binding.pickerIcon.setOnSelectedIconListener {
            viewModel.icon.value = it
        }

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputName.error = it.name.first()
            binding.inputInitialValue.error = it.initialValue.first()
            binding.inputDescription.error = it.description.first()
            if (it.icon.first() != null) {
                binding.pickerIcon.setError()
                binding.pickerIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.md_theme_error))
            }
        }

        binding.buttonAdd.setOnClickListener {
            viewModel.submit(
                binding.editName.text.toString(),
                binding.editInitialValue.text.toString(),
                binding.editDescription.text.toString()
            ) {
                Snackbar.make(
                    binding.buttonAdd,
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