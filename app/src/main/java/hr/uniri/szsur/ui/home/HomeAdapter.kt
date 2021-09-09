package hr.uniri.szsur.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.uniri.szsur.data.model.Event
import hr.uniri.szsur.data.repository.UserRepository
import hr.uniri.szsur.databinding.LayoutCardEventBinding
import hr.uniri.szsur.util.DiffCallback
import hr.uniri.szsur.util.handleClick

class HomeAdapter(
    private val context: Context?,
    private val showDetailsListener: (event: Event) -> Unit
) : ListAdapter<Event, HomeAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(
        private val context: Context?,
        private var binding: LayoutCardEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            binding.event = event
            binding.isFavorite = UserRepository.user.value!!.favorites.contains(event.documentId)
            binding.favoritesButton.setOnClickListener {
                handleClick(event.documentId, true, context)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutCardEventBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(context, binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val event = getItem(position)
        viewHolder.itemView.setOnClickListener { showDetailsListener(event) }
        viewHolder.bind(event)
    }
}
