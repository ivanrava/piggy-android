package dev.ivanravasi.piggy.ui.accounts

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.skydoves.colorpickerview.ColorEnvelope
import dev.ivanravasi.piggy.R
import dev.ivanravasi.piggy.api.GsonProvider
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.requests.AccountRequest
import dev.ivanravasi.piggy.data.DataStoreRepository
import dev.ivanravasi.piggy.databinding.FragmentAddAccountBinding
import dev.ivanravasi.piggy.ui.accountTextColor
import dev.ivanravasi.piggy.ui.accounts.dialogs.ColorPickerDialog
import dev.ivanravasi.piggy.ui.backWithSnackbar


class AddAccountFragment : Fragment() {
    private var accountToUpdate: Account? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAddAccountBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, AddAccountViewModel.Factory(
            DataStoreRepository(requireContext()),
            ContextCompat.getColor(requireContext(), R.color.md_theme_primary),
            findNavController()
        ))[AddAccountViewModel::class.java]

        binding.editOpening.setToday()

        binding.pickerIcon.setOnSelectedIconListener {
            viewModel.icon.value = it
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
            if (it.isEmpty())
                return@observe

            val types = it.map { it.type }.toTypedArray()
            binding.editAccountType.setSimpleItems(types)
            binding.editAccountType.setText(types.first(), false)
        }

        binding.pickerColor.setOnClickListener {
            ColorPickerDialog().show(requireContext()) {
                viewModel.color.value = it
            }
        }
        viewModel.color.observe(viewLifecycleOwner) {
            binding.pickerColor.backgroundTintList = ColorStateList.valueOf(it.color)
            binding.pickerColor.iconTint = ColorStateList.valueOf(accountTextColor(requireContext(), it.hexCode))
        }

        val accountStr = arguments?.getString("account")
        accountStr?.let {
            accountToUpdate = GsonProvider.getDeserializer().fromJson(it, Account::class.java)

            binding.editName.setText(accountToUpdate!!.name)
            binding.editAccountType.setText(accountToUpdate!!.type)
            binding.editOpening.setText(accountToUpdate!!.opening)
            binding.editClosing.setText(accountToUpdate!!.closing)
            binding.editInitialBalance.setText(accountToUpdate!!.initialBalance)
            binding.editDescription.setText(accountToUpdate!!.description)
            binding.pickerIcon.loadIconify(accountToUpdate!!.icon)
            viewModel.color.value = ColorEnvelope(Color.parseColor(accountToUpdate!!.color))

            binding.buttonAdd.text = requireContext().getString(R.string.update_account)
            binding.addTitle.text = requireContext().getString(R.string.update_account)
        }

        binding.buttonAdd.setOnClickListener {
            val request = AccountRequest(
                binding.editName.text.toString(),
                viewModel.icon.value,
                "#${viewModel.color.value!!.hexCode.substring(2)}",
                binding.editOpening.date(),
                binding.editClosing.date(),
                binding.editInitialBalance.text.toString(),
                binding.editDescription.text.toString(),
                viewModel.accountTypes.value?.find { it.type == binding.editAccountType.text.toString() }?.id!!
            )
            if (accountToUpdate == null) {
                viewModel.submit(request) {
                    backWithSnackbar(binding.buttonAdd, "Account added successfully")
                }
            } else {
                viewModel.update(request, accountToUpdate!!.id) {
                    backWithSnackbar(binding.buttonAdd, "Account updated ${request.name} successfully")
                }
            }
        }

        return binding.root
    }
}