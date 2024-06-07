package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Property
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentPropertiesBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment

class PropertiesFragment : CRUDFragment<Property, PropertyAdapter.PropertyViewHolder>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPropertiesBinding.inflate(inflater, container, false)
        val viewModel = PropertiesViewModel(TokenRepository(requireContext()))

        val adapter = PropertyAdapter()
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