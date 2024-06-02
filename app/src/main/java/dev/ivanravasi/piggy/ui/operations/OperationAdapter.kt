package dev.ivanravasi.piggy.ui.operations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.dicebear.loadBeneficiary
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Operation
import dev.ivanravasi.piggy.api.piggy.bodies.entities.OperationType
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transaction
import dev.ivanravasi.piggy.api.piggy.bodies.entities.Transfer
import dev.ivanravasi.piggy.databinding.ListItemTransactionBinding
import dev.ivanravasi.piggy.databinding.ListItemTransferBinding
import dev.ivanravasi.piggy.ui.setAccount
import dev.ivanravasi.piggy.ui.setCurrency
import dev.ivanravasi.piggy.ui.setDate

class OperationAdapter: ListAdapter<Operation, OperationAdapter.OperationViewHolder>(OperationDiffCallback()) {
    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = getItem(position)
        holder.bind(operation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        return if (viewType == OperationType.TRANSACTION.ordinal)
            TransactionViewHolder.from(parent)
        else
            TransferViewHolder.from(parent)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type().ordinal
    }

    abstract class OperationViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        abstract fun bind(operation: Operation)
    }

    class TransactionViewHolder private constructor(
        private val binding: ListItemTransactionBinding
    ): OperationViewHolder(root = binding.root) {
        private fun bind(transaction: Transaction) {
            binding.beneficiaryImg.loadBeneficiary(img = transaction.beneficiary.img, seed = transaction.beneficiary.name)
            binding.beneficiaryName.text = transaction.beneficiary.name

            binding.categoryIcon.loadIconify(transaction.category.icon, binding.categoryName.currentTextColor)
            binding.categoryName.text = transaction.category.name

            binding.notes.text = ""
            transaction.notes?.let {
                binding.notes.text = "\"${transaction.notes}\""
            }

            binding.value.setCurrency(transaction.amount.toDouble() * if (transaction.category.type == "out") -1 else 1, true)
            binding.date.setDate(transaction.date)
        }

        companion object {
            fun from(parent: ViewGroup): TransactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransactionBinding.inflate(layoutInflater, parent, false)
                return TransactionViewHolder(binding)
            }
        }

        override fun bind(operation: Operation) {
            bind(operation as Transaction)
        }
    }

    class TransferViewHolder private constructor(
        private val binding: ListItemTransferBinding,
        private val navController: NavController
    ): OperationViewHolder(root = binding.root) {
        private fun bind(transfer: Transfer) {
            if (transfer.to != null)
                binding.cardAccount.setAccount(transfer.to, navController)
            else if (transfer.from != null)
                binding.cardAccount.setAccount(transfer.from, navController)
            else
                binding.cardAccount.cardAccount.visibility = View.GONE

            binding.value.setCurrency(transfer.amount.toDouble() * if (transfer.to != null) -1 else 1, true)
            binding.date.setDate(transfer.date)
        }

        companion object {
            fun from(parent: ViewGroup): TransferViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTransferBinding.inflate(layoutInflater, parent, false)
                val navController = parent.findFragment<OperationsFragment>().findNavController()
                return TransferViewHolder(binding, navController)
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