package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentBeneficiariesBinding
import dev.ivanravasi.piggy.ui.common.CRUDFragment

private const val SPAN_COUNT = 5

class BeneficiariesFragment : CRUDFragment<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBeneficiariesBinding.inflate(inflater, container, false)
        val viewModel = BeneficiariesViewModel(TokenRepository(requireContext()))

        val manager = GridLayoutManager(activity, SPAN_COUNT)
        binding.listBeneficiaries.layoutManager = manager

        val adapter = BeneficiaryAdapter(object : OnBeneficiaryClickListener {
            override fun onBeneficiaryClick(beneficiary: Beneficiary) {
                ShowBeneficiaryBottomSheet(beneficiary, parentFragmentManager).show()
            }
        })
        setup(
            list = binding.listBeneficiaries,
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