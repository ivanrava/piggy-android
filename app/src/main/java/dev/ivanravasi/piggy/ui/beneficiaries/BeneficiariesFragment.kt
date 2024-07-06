package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentBeneficiariesBinding
import dev.ivanravasi.piggy.ui.beneficiaries.dialogs.ShowBeneficiaryBottomSheet
import dev.ivanravasi.piggy.ui.common.fragments.CRUDFragment
import dev.ivanravasi.piggy.ui.makeSnackbar

private const val SPAN_COUNT = 5

class BeneficiariesFragment : CRUDFragment<Beneficiary, BeneficiaryAdapter.BeneficiaryViewHolder>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBeneficiariesBinding.inflate(inflater, container, false)
        val viewModel = BeneficiariesViewModel(DataStoreRepository(requireContext()), findNavController())

        val manager = GridLayoutManager(activity, SPAN_COUNT)
        binding.listBeneficiaries.layoutManager = manager

        val adapter = BeneficiaryAdapter { beneficiary ->
            ShowBeneficiaryBottomSheet(beneficiary, parentFragmentManager, {
                val bundle = Bundle()
                bundle.putString("beneficiary", GsonProvider.getSerializer().toJson(beneficiary))
                findNavController().navigate(R.id.navigation_add_beneficiary, bundle)
            }, {
                viewModel.delete(it.id, "beneficiaries")
                makeSnackbar(binding.root, "Beneficiary \"${it.name}\" deleted successfully")
            }).show()
        }
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