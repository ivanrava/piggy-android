package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentPropertiesBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment
import dev.ivanravasi.piggy.ui.makeSnackbar

class PropertiesFragment : CRUDFragment<Property, PropertyAdapter.PropertyViewHolder>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPropertiesBinding.inflate(inflater, container, false)
        val viewModel = PropertiesViewModel(TokenRepository(requireContext()), findNavController())

        val adapter = PropertyAdapter(object : OnPropertyClickListener {
            override fun onPropertyClick(property: Property) {
                ShowPropertyBottomSheet(property, parentFragmentManager, {
                    val bundle = Bundle()
                    bundle.putString("property", GsonBuilder().create().toJson(property))
                    findNavController().navigate(R.id.navigation_add_property, bundle)
                }, {
                    viewModel.delete(it.id, "properties")
                    makeSnackbar(binding.root, "Property \"${it.name}\" deleted successfully")
                }).show()
            }
        }) {
            makeSnackbar(binding.root, "Property variation added successfully")
            viewModel.refreshAll()
        }
        setup(
            list = binding.listProperties,
            adapter = adapter,
            viewModel = viewModel,
            noDataView = binding.nodata,
            loadingProgressIndicator = binding.loadingProgress
        ) {
            adapter.submitList(it)
        }

        return binding.root
    }
}