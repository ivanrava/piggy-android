package dev.ivanravasi.piggy.ui.properties

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddPropertyBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar

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
            val request = PropertyRequest(
                binding.editName.text.toString(),
                viewModel.icon.value,
                binding.editInitialValue.text.toString(),
                binding.editDescription.text.toString()
            )
            viewModel.submit(request) {
                backWithSnackbar(binding.buttonAdd, "Property added successfully")
            }
        }

        return binding.root
    }
}