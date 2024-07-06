package dev.ivanravasi.piggy.ui.properties.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentPropertiesBinding
import dev.ivanravasi.piggy.ui.common.fragments.CRUDFragment
import dev.ivanravasi.piggy.ui.confirmDeleteWithDialog
import dev.ivanravasi.piggy.ui.makeSnackbar

class PropertiesFragment : CRUDFragment<Property, PropertyAdapter.PropertyViewHolder>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPropertiesBinding.inflate(inflater, container, false)
        val viewModel = PropertiesViewModel(DataStoreRepository(requireContext()), findNavController())

        val adapter = PropertyAdapter({
            makeSnackbar(binding.root, "Property variation added successfully")
            viewModel.refreshAll()
        }) { property ->
            ShowPropertyBottomSheet(property, parentFragmentManager, {
                val bundle = Bundle()
                bundle.putString("property", GsonProvider.getSerializer(true).toJson(property))
                findNavController().navigate(R.id.navigation_add_property, bundle)
            }, {
                confirmDeleteWithDialog(it.name) {
                    viewModel.delete(it.id, "properties")
                    makeSnackbar(binding.root, "Property \"${it.name}\" deleted successfully")
                }
            }).show()
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