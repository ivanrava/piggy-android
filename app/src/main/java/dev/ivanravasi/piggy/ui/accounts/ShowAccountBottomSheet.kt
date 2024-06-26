package dev.ivanravasi.piggy.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.databinding.BottomSheetAccountBinding
import dev.ivanravasi.piggy.ui.setAccount

class ShowAccountBottomSheet(
    private val account: Account,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (account: Account) -> Unit,
    private val onDelete: (account: Account) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetAccountBinding.inflate(inflater, container, false)

        binding.cardAccount.setAccount(account)

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(account)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(account)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowBeneficiaryBottomSheet")
    }
}
