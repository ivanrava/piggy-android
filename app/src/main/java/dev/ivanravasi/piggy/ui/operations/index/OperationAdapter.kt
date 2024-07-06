package dev.ivanravasi.piggy.ui.operations.index

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.CategoryType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.api.piggy.bodies.entities.OperationType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.databinding.ListItemTransactionBinding
import dev.ivanravasi.piggy.databinding.ListItemTransferBinding
import dev.ivanravasi.piggy.ui.setAccount
import dev.ivanravasi.piggy.ui.setCurrency
import dev.ivanravasi.piggy.ui.setDate

class OperationAdapter(
    private val onTransactionClicked: (transaction: Transaction) -> Unit,
    private val onTransferClicked: (transfer: Transfer) -> Unit,
): ListAdapter<Operation, OperationAdapter.OperationViewHolder>(OperationDiffCallback()) {
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = getItem(position)
        holder.bind(operation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return if (viewType == OperationType.TRANSACTION.ordinal)
            TransactionViewHolder.from(parent, onTransactionClicked)
        else
            TransferViewHolder.from(parent, onTransferClicked)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type().ordinal
    }

    abstract class OperationViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val UNCHECKED_ALPHA = 0.2f
        abstract fun bind(operation: Operation)
        fun alpha(checked: Boolean) = if (checked) 1.0f else UNCHECKED_ALPHA
    }

    class TransactionViewHolder private constructor(
        private val binding: ListItemTransactionBinding,
        private val onTransactionClicked: (transaction: Transaction) -> Unit
    ): OperationViewHolder(root = binding.root) {
        private fun bind(transaction: Transaction) {
            binding.cardBeneficiary.beneficiaryImg.loadBeneficiary(img = transaction.beneficiary.img, seed = transaction.beneficiary.name)
            binding.beneficiaryName.text = transaction.beneficiary.name

            binding.categoryIcon.loadIconify(transaction.category.icon, binding.categoryName.currentTextColor)
            binding.categoryName.text = transaction.category.name

            binding.notes.text = ""
            transaction.notes?.let {
                binding.notes.text = "\"${transaction.notes}\""
            }

            binding.value.setCurrency(transaction.amount.toDouble() * if (transaction.category.type() == CategoryType.OUT) -1 else 1, true)
            binding.value.alpha = alpha(transaction.checked)
            binding.date.setDate(transaction.date)

            binding.root.setOnClickListener {
                onTransactionClicked(transaction)
            }
        }

        companion object {
            fun from(parent: ViewGroup, onOperationClicked: (transaction: Transaction) -> Unit): TransactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransactionBinding.inflate(layoutInflater, parent, false)
                return TransactionViewHolder(binding, onOperationClicked)
            }
        }

        override fun bind(operation: Operation) {
            bind(operation as Transaction)
        }
    }

    class TransferViewHolder private constructor(
        private val binding: ListItemTransferBinding,
        private val onTransferClicked: (transfer: Transfer) -> Unit
    ): OperationViewHolder(root = binding.root) {
        private fun bind(transfer: Transfer) {
            if (transfer.to != null)
                binding.cardAccount.setAccount(transfer.to)
            else if (transfer.from != null)
                binding.cardAccount.setAccount(transfer.from)
            else
                binding.cardAccount.cardAccount.visibility = View.GONE

            binding.value.setCurrency(transfer.amount.toDouble() * if (transfer.to != null) -1 else 1, true)
            binding.value.alpha = alpha(transfer.checked)
            binding.date.setDate(transfer.date)

            binding.root.setOnClickListener {
                onTransferClicked(transfer)
            }
        }

        companion object {
            fun from(parent: ViewGroup, onOperationClicked: (transfer: Transfer) -> Unit): TransferViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransferBinding.inflate(layoutInflater, parent, false)
                parent.findFragment<OperationsFragment>().findNavController()
                return TransferViewHolder(binding, onOperationClicked)
            }
        }

        override fun bind(operation: Operation) {
            bind(operation as Transfer)
        }
    }
}

class OperationDiffCallback : DiffUtil.ItemCallback<Operation>() {
    override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem.getOperationId() == newItem.getOperationId() && oldItem.type() == newItem.type()
    }

    override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        if (oldItem.type() != newItem.type())
            return false
        else if (oldItem.type() == OperationType.TRANSFER)
            return areContentsTheSame(oldItem as Transfer, newItem as Transfer)
        else
            return areContentsTheSame(oldItem as Transaction, newItem as Transaction)
    }

    private fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    private fun areContentsTheSame(oldItem: Transfer, newItem: Transfer): Boolean {
        return oldItem == newItem
    }
}