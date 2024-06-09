package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.dicebear.loadAvatar
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.dicebear.loadFallback
import dev.ivanravasi.piggy.api.piggy.bodies.requests.BeneficiaryRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddBeneficiaryBinding
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced
import dev.ivanravasi.piggy.ui.backWithSnackbar


class AddBeneficiaryFragment : Fragment() {
    private lateinit var binding: FragmentAddBeneficiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBeneficiaryBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddBeneficiaryViewModel.Factory(
            TokenRepository(requireContext()),
        ))[AddBeneficiaryViewModel::class.java]

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputName.error = it.name.first()
            binding.inputDomain.error = it.img.first()
        }

        binding.beneficiaryType.addOnButtonCheckedListener { _, _, _ ->
            updateBeneficiaryFrontend()
        }
        binding.editName.afterTextChangedDebounced {
            updateBeneficiaryFrontend()
        }
        binding.editDomain.afterTextChangedDebounced {
            updateBeneficiaryFrontend()
        }
        binding.beneficiaryType.check(R.id.people)

        binding.buttonAdd.setOnClickListener {
            val request = BeneficiaryRequest(
                binding.editName.text.toString(),
                when (binding.beneficiaryType.checkedButtonId) {
                    R.id.people -> "bottts"
                    R.id.companies -> "https://logo.clearbit.com/${binding.editDomain.text}"
                    R.id.generic -> "shapes"
                    else -> null
                }
            )
            viewModel.submit(request) {
                backWithSnackbar(binding.buttonAdd, "Beneficiary added successfully")
            }
        }

        return binding.root
    }

    private fun updateBeneficiaryFrontend() {
        when (binding.beneficiaryType.checkedButtonId) {
            R.id.people -> {
                binding.inputDomain.visibility = View.GONE
                binding.beneficiaryImg.loadAvatar("bottts", binding.editName.text.toString())
            }
            R.id.companies -> {
                binding.inputDomain.visibility = View.VISIBLE
                binding.beneficiaryImg.loadBeneficiary(
                    "https://logo.clearbit.com/${binding.editDomain.text}",
                    binding.editName.text.toString()
                )
            }
            R.id.generic -> {
                binding.inputDomain.visibility = View.GONE
                binding.beneficiaryImg.loadAvatar("shapes", binding.editName.text.toString())
            }
        }
    }
}