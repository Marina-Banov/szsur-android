package hr.uniri.szsur.util

import androidx.recyclerview.widget.DiffUtil
import hr.uniri.szsur.data.model.Filterable

class DiffCallback<T: Filterable> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.documentId == newItem.documentId
    }
}