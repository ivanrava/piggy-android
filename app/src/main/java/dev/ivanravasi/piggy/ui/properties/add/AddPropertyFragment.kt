package dev.ivanravasi.piggy.ui.properties.add

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.api.piggy.bodies.requests.PropertyRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddPropertyBinding
import dev.ivanravasi.piggy.ui.backWithSnackbar

class AddPropertyFragment : Fragment() {
    private var propertyToUpdate: Property? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding and view model
        val binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        val viewModel = AddPropertyViewModel(DataStoreRepository(requireContext()), findNavController())

        // Set listeners / observers
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

        val propertyStr = arguments?.getString("property")
        propertyStr?.let {
            propertyToUpdate = GsonBuilder().create().fromJson(it, Property::class.java)

            binding.editName.setText(propertyToUpdate!!.name)
            binding.pickerIcon.loadIconify(propertyToUpdate!!.icon)

            binding.editInitialValue.setText(propertyToUpdate!!.initialValue)
            binding.editDescription.setText(propertyToUpdate!!.description)

            binding.buttonAdd.text = requireContext().getString(R.string.update_property)
            binding.addTitle.text = requireContext().getString(R.string.update_property)
        }

        binding.buttonAdd.setOnClickListener {
            val request = PropertyRequest(
                binding.editName.text.toString(),
                viewModel.icon.value,
                binding.editInitialValue.text.toString(),
                binding.editDescription.text.toString()
            )
            if (propertyToUpdate == null) {
                viewModel.submit(request) {
                    backWithSnackbar(binding.buttonAdd, "Property added successfully")
                }
            } else {
                viewModel.update(request, propertyToUpdate!!.id) {
                    backWithSnackbar(binding.buttonAdd, "Property ${request.name} updated successfully")
                }
            }
        }

        return binding.root
    }
}