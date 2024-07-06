package dev.ivanravasi.piggy.ui.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.dicebear.loadAvatar
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Beneficiary
import dev.ivanravasi.piggy.api.piggy.bodies.entities.BeneficiaryType
import dev.ivanravasi.piggy.api.piggy.bodies.requests.BeneficiaryRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddBeneficiaryBinding
import dev.ivanravasi.piggy.ui.afterTextChangedDebounced
import dev.ivanravasi.piggy.ui.backWithSnackbar


class AddBeneficiaryFragment : Fragment() {
    private var beneficiaryToUpdate: Beneficiary? = null
    private lateinit var binding: FragmentAddBeneficiaryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize binding and view model
        binding = FragmentAddBeneficiaryBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddBeneficiaryViewModel.Factory(
            DataStoreRepository(requireContext()),
            findNavController()
        ))[AddBeneficiaryViewModel::class.java]

        // Set default initial values
        binding.beneficiaryType.check(R.id.people)

        // Set listeners / observers
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

        // Set initial values from editable (if present)
        val beneficiaryStr = arguments?.getString("beneficiary")
        if (beneficiaryStr == null) {
            updateBeneficiaryFrontend()
        } else {
            beneficiaryToUpdate = GsonProvider.getDeserializer().fromJson(beneficiaryStr, Beneficiary::class.java)

            binding.beneficiaryType.check(when (beneficiaryToUpdate!!.type()) {
                BeneficiaryType.PEOPLE -> R.id.people
                BeneficiaryType.COMPANY -> R.id.companies
                BeneficiaryType.GENERIC -> R.id.generic
            })

            binding.editName.setText(beneficiaryToUpdate!!.name)
            if (beneficiaryToUpdate!!.type() == BeneficiaryType.COMPANY && !beneficiaryToUpdate!!.isWithoutRemoteImg()) {
                binding.editDomain.setText(beneficiaryToUpdate!!.img)
            }

            binding.buttonAdd.text = requireContext().getString(R.string.button_update_beneficiary)
            binding.addTitle.text = requireContext().getString(R.string.button_update_beneficiary)
        }

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
            if (beneficiaryToUpdate == null) {
                viewModel.submit(request) {
                    backWithSnackbar(binding.buttonAdd, "Beneficiary added successfully")
                }
            } else {
                viewModel.update(request, beneficiaryToUpdate!!.id) {
                    backWithSnackbar(binding.buttonAdd, "Beneficiary ${request.name} updated successfully")
                }
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