package dev.ivanravasi.piggy.ui.accounts

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.data.TokenRepository
import dev.ivanravasi.piggy.databinding.FragmentAddAccountBinding
import dev.ivanravasi.piggy.ui.accountTextColor
import dev.ivanravasi.piggy.ui.backWithSnackbar


class AddAccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddAccountViewModel.Factory(
            TokenRepository(requireContext()),
            ContextCompat.getColor(requireContext(), R.color.md_theme_primary)
        ))[AddAccountViewModel::class.java]

        binding.pickerIcon.setOnSelectedIconListener {
            viewModel.icon.value = it
        }
        viewModel.icon.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.pickerIcon.loadIconify(it)
            }
        }

        viewModel.errors.observe(viewLifecycleOwner) {
            binding.inputName.error = it.name.first()
            binding.inputInitialBalance.error = it.initialBalance.first()
            binding.inputOpening.error = it.opening.first()
            binding.inputClosing.error = it.closing.first()
            binding.inputAccountType.error = it.accountTypeId.first()
            binding.inputDescription.error = it.description.first()
            if (it.icon.first() != null) {
                binding.pickerIcon.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.md_theme_error))
            }
        }

        viewModel.accountTypes.observe(viewLifecycleOwner) {
            (binding.editAccountType as? MaterialAutoCompleteTextView)?.setSimpleItems(it.map { it.type }.toTypedArray())
        }

        binding.editOpening.setToday()

        binding.pickerColor.setOnClickListener {
            ColorPickerDialog().show(requireContext()) {
                viewModel.color.value = it
            }
        }
        viewModel.color.observe(viewLifecycleOwner) {
            binding.pickerColor.backgroundTintList = ColorStateList.valueOf(it.color)
            binding.pickerColor.iconTint = ColorStateList.valueOf(accountTextColor(requireContext(), it.hexCode))
        }

        binding.buttonAdd.setOnClickListener {
            if (binding.editAccountType.text.isEmpty()) {
                binding.editAccountType.error = "You need to specify an account type"
                return@setOnClickListener
            }
            val request = AccountRequest(
                binding.editName.text.toString(),
                viewModel.icon.value,
                "#${viewModel.color.value?.hexCode}",
                binding.editOpening.date(),
                binding.editClosing.date(),
                binding.editInitialBalance.text.toString(),
                binding.editDescription.text.toString(),
                viewModel.accountTypes.value?.find { it.type == binding.editAccountType.text.toString() }?.id!!
            )
            viewModel.submit(request) {
                backWithSnackbar(binding.buttonAdd, "Account added successfully")
            }
        }

        return binding.root
    }
}