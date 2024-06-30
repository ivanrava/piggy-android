package dev.ivanravasi.piggy.ui.operations.index.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Account
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.databinding.BottomSheetTransferBinding
import dev.ivanravasi.piggy.ui.setAccount
import dev.ivanravasi.piggy.ui.setCurrency
import dev.ivanravasi.piggy.ui.setDate

class ShowTransferBottomSheet(
    private val account: Account,
    private val transfer: Transfer,
    private val fragmentManager: FragmentManager,
    private val onUpdate: (transfer: Transfer) -> Unit,
    private val onDelete: (transfer: Transfer) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = BottomSheetTransferBinding.inflate(inflater, container, false)

        binding.from.setAccount(transfer.from ?: account)
        binding.to.setAccount(transfer.to ?: account)
        binding.amount.setCurrency(transfer.amount.toDouble() * if (transfer.to == null) 1 else -1, true)
        binding.date.setDate(transfer.date)
        binding.notes.text = transfer.notes
        if (transfer.notes == null || transfer.notes.isEmpty()) {
            binding.notes.visibility = View.GONE
        }

        binding.btnUpdate.setOnClickListener {
            dismiss()
            onUpdate(transfer)
        }
        binding.btnDelete.setOnClickListener {
            dismiss()
            onDelete(transfer)
        }

        return binding.root
    }

    fun show() {
        show(fragmentManager, "ShowTransferBottomSheet")
    }
}
