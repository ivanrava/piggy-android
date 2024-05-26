package dev.ivanravasi.piggy.ui.properties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentPropertiesBinding

class PropertiesFragment : Fragment() {
    private lateinit var viewModel: PropertiesViewModel
    private lateinit var binding: FragmentPropertiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertiesBinding.inflate(inflater, container, false)
        viewModel = PropertiesViewModel(TokenRepository(requireContext()))

        viewModel.properties.observe(viewLifecycleOwner) {
            binding.res.text = it.toString()
        }

        return binding.root
    }
}