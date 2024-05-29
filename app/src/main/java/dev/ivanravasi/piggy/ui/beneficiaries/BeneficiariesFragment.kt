package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentBeneficiariesBinding

private const val SPAN_COUNT = 5

class BeneficiariesFragment : Fragment() {
    private lateinit var viewModel: BeneficiariesViewModel
    private lateinit var binding: FragmentBeneficiariesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBeneficiariesBinding.inflate(inflater, container, false)
        viewModel = BeneficiariesViewModel(TokenRepository(requireContext()))

        val manager = GridLayoutManager(activity, SPAN_COUNT)
        binding.listBeneficiaries.layoutManager = manager

        val adapter = BeneficiaryAdapter()
        binding.listBeneficiaries.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.loadingProgress.show()
            else
                binding.loadingProgress.hide()
        }
        viewModel.beneficiaries.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}