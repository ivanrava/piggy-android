package dev.ivanravasi.piggy.ui.common.iconify

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ivanravasi.piggy.api.iconify.loadIconify
import dev.ivanravasi.piggy.databinding.ListItemIconBinding

interface OnIconClickListener {
    fun onIconClick(icon: String)
}

class IconAdapter(
    private val color: Int,
    private val iconClickListener: OnIconClickListener
): ListAdapter<String, IconAdapter.IconifyIconViewHolder>(IconDiffCallback()) {
    override fun onBindViewHolder(holder: IconifyIconViewHolder, position: Int) {
        val icon = getItem(position)
        holder.bind(icon, color, iconClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconifyIconViewHolder {
        return IconifyIconViewHolder.from(parent)
    }

    class IconifyIconViewHolder private constructor(
        val binding: ListItemIconBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(icon: String, color: Int, iconClickListener: OnIconClickListener) {
            binding.icon.loadIconify(icon, color)
            binding.icon.setOnClickListener {
                iconClickListener.onIconClick(icon)
            }
        }

        companion object {
            fun from(parent: ViewGroup): IconifyIconViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemIconBinding.inflate(layoutInflater, parent, false)
                return IconifyIconViewHolder(binding)
            }
        }
    }
}

class IconDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
